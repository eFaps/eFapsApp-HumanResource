/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.efaps.esjp.humanresource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.admin.ui.AbstractCommand;
import org.efaps.admin.ui.AbstractUserInterfaceObject.TargetMode;
import org.efaps.admin.ui.field.Field;
import org.efaps.db.Checkin;
import org.efaps.db.Context;
import org.efaps.db.Context.FileParameter;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.db.Update;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.common.file.FileUtil;
import org.efaps.esjp.common.file.ImageField;
import org.efaps.esjp.humanresource.util.HumanResource;
import org.efaps.util.EFapsException;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * TODO comment! First fast draft for testing purpose only!!!
 *
 * @author The eFaps Team
 */
@EFapsUUID("33c5bee0-f958-4326-90d7-173bcf77476c")
@EFapsApplication("eFapsApp-HumanResource")
public abstract class Image_Base
{

    /**
     * Access is defined by a SystemConfiguration.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return Return with true if access is granted
     * @throws EFapsException on error
     */
    public Return access4Image(final Parameter _parameter)
        throws EFapsException
    {
        final Return ret = new Return();
        if (HumanResource.ACTIVATEIMAGE.get()) {
            ret.put(ReturnValues.TRUE, true);
        }
        return ret;
    }

    /**
     * Grants access to the field used as a link to the original file.
     * @param _parameter Parameter as passed by the eFaps API
     * @return Return with true if access is granted
     * @throws EFapsException on error
     */
    public Return access4OriginalFieldUI(final Parameter _parameter)
        throws EFapsException
    {
        final Return ret = new Return();
        final Instance instance = _parameter.getInstance();
        if (instance != null && instance.isValid()) {
            if (instance.getType().getAttribute("Original") != null) {
                ret.put(ReturnValues.TRUE, true);
            }
        }
        return ret;
    }

    /**
     * Grants access to the field used as a link to the original file.
     * @param _parameter Parameter as passed by the eFaps API
     * @return Return with true if access is granted
     * @throws EFapsException on error
     */
    public Return getImageFieldValueUI(final Parameter _parameter)
        throws EFapsException
    {
        final Map<?, ?> properties = (Map<?, ?>) _parameter.get(ParameterValues.PROPERTIES);
        final Type type;
        if (properties.containsKey("RelationType")) {
            type = Type.get((String) properties.get("RelationType"));
        } else {
            type = CIHumanResource.Employee2ImageAbstract.getType();
        }
        final QueryBuilder queryBldr = new QueryBuilder(type);
        queryBldr.addWhereAttrEqValue(CIHumanResource.Employee2ImageAbstract.EmployeeAbstractLink,
                        _parameter.getInstance());
        final MultiPrintQuery multi = queryBldr.getPrint();
        final SelectBuilder selImageInst = new SelectBuilder()
                                    .linkto(CIHumanResource.Employee2ImageAbstract.ImageAbstractLink).instance();
        multi.addSelect(selImageInst);
        multi.execute();
        final String imageOid;
        final String linkOid;
        if (multi.next()) {
            final Instance imageInst = multi.<Instance>getSelect(selImageInst);
            imageOid = imageInst.getOid();
            if (imageInst.getType().equals(CIHumanResource.ImageThumbnail.getType())) {
                final PrintQuery imPrint = new PrintQuery(imageInst);
                final SelectBuilder selLinkOid = new SelectBuilder()
                                .linkto(CIHumanResource.ImageThumbnail.OriginalLink).oid();
                imPrint.addSelect(selLinkOid);
                imPrint.execute();
                linkOid = imPrint.<String>getSelect(selLinkOid);
            } else {
                linkOid = imageOid;
            }
        } else {
            linkOid = null;
            imageOid = null;
        }
        Return retVal;
        if (imageOid != null) {
            retVal = new ImageField(){

                @Override
                protected String getImageOid(final Parameter _parameter)
                    throws EFapsException
                {
                    return imageOid ;
                }

                @Override
                protected String getTargetOid(final Parameter _parameter)
                    throws EFapsException
                {
                    return linkOid ;
                }

            }.getViewFieldValueUI(_parameter);
        } else {
            retVal = new Return();
            retVal.put(ReturnValues.SNIPLETT, "");
        }
        return retVal;
    }


    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @return Return with true if access is granted
     * @throws EFapsException on error
     */
    public Return create(final Parameter _parameter)
        throws EFapsException
    {
        final Instance parentInst = _parameter.getInstance();

        final Instance imageInst = createImage(_parameter, CIHumanResource.ImageOriginal.getType(), null);
        connectImage(_parameter, CIHumanResource.Employee2ImageOriginal.getType(), parentInst, imageInst);
        final FileParameter fileItem = getFileParameter(_parameter);
        uploadImage(_parameter, imageInst, fileItem);
        final Properties props = HumanResource.IMAGEPROPERTIES.get();
        if (props.containsKey("Image4Doc_Create") && "true".equalsIgnoreCase(props.getProperty("Image4Doc_Create"))) {
            final int width = Integer.parseInt(props.getProperty("Image4Doc_Width", "250"));
            final int height = Integer.parseInt(props.getProperty("Image4Doc_Height", "250"));
            final boolean enlarge = "true".equalsIgnoreCase(props.getProperty("Image4Doc_Enlarge", "false"));
            final DimensionConstrain dim = DimensionConstrain.createMaxDimension(width, height, !enlarge);
            final File img = createNewImageFile(_parameter, "Image4Doc_", fileItem, dim);
            final Instance copyInst = createImage(_parameter, CIHumanResource.ImageDocument.getType(), imageInst);
            connectImage(_parameter, CIHumanResource.Employee2ImageDocument.getType(), parentInst, copyInst);
            uploadImage(_parameter, copyInst, img);
        }
        if (props.containsKey("Thumbnail_Create") && "true".equalsIgnoreCase(props.getProperty("Thumbnail_Create"))) {
            final int width = Integer.parseInt(props.getProperty("Thumbnail_Width", "150"));
            final int height = Integer.parseInt(props.getProperty("Thumbnail_Height", "150"));
            final boolean enlarge = "true".equalsIgnoreCase(props.getProperty("Thumbnail_Enlarge", "false"));
            final DimensionConstrain dim = DimensionConstrain.createMaxDimension(width, height, !enlarge);
            final File img = createNewImageFile(_parameter, "Thumbnail_", fileItem, dim);
            final Instance copyInst = createImage(_parameter, CIHumanResource.ImageThumbnail.getType(), imageInst);
            connectImage(_parameter, CIHumanResource.Employee2ImageThumbnail.getType(), parentInst, copyInst);
            uploadImage(_parameter, copyInst, img);
        }
        return new Return();
    }

    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _prefix       Prefix
     * @param _fileItem     FileItem the new image will be created from
     * @param _dim          Dimension of the new image
     * @throws EFapsException on error
     * @return new File
     */
    @SuppressWarnings("rawtypes")
    protected File createNewImageFile(final Parameter _parameter,
                                      final String _prefix,
                                      final FileParameter _fileItem,
                                      final DimensionConstrain _dim)
        throws EFapsException
    {
        final File ret;
        try {
            final ImageInfo info = Imaging.getImageInfo(_fileItem.getInputStream(), _fileItem.getName());
            ret = new FileUtil().getFile(_prefix + _fileItem.getName(), info.getFormat().getDefaultExtension());

            final FileOutputStream os = new FileOutputStream(ret);

            if (info.getFormat().equals(ImageFormats.JPEG)) {
                final BufferedImage img = ImageIO.read(_fileItem.getInputStream());
                final ResampleOp resampleOp = new ResampleOp(_dim);
                final BufferedImage rescaledTomato = resampleOp.filter(img, null);
                Imaging.writeImage(rescaledTomato, os, info.getFormat());
            } else {
                final BufferedImage image = Imaging.getBufferedImage(_fileItem.getInputStream());
                final ResampleOp resampleOp = new ResampleOp(_dim);
                // resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
                final BufferedImage rescaledTomato = resampleOp.filter(image, null);
                Imaging.writeImage(rescaledTomato, os, info.getFormat());
            }
        } catch (final IOException e) {
            throw new EFapsException(this.getClass(), "createNewImage", e, _parameter);
        }
        return ret;
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @return FileParameter from the context
     * @throws EFapsException on error
     */
    protected FileParameter getFileParameter(final Parameter _parameter)
        throws EFapsException
    {
        final Context context = Context.getThreadContext();

        final AbstractCommand command = (AbstractCommand) _parameter.get(ParameterValues.UIOBJECT);
        FileParameter ret = null;
        for (final Field field : command.getTargetForm().getFields()) {
            final String attrName = field.getAttribute();
            if (attrName == null && field.isEditableDisplay(TargetMode.CREATE)) {
                final FileParameter fileItem = context.getFileParameters().get(field.getName());
                if (fileItem != null) {
                    ret = fileItem;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _imageInst    Instance the imgae will be check in to
     * @param _fileItem      file to be checked in
     * @throws EFapsException on error
     */
    protected void uploadImage(final Parameter _parameter,
                               final Instance _imageInst,
                               final FileParameter _fileItem)
        throws EFapsException
    {
        final Checkin checkin = new Checkin(_imageInst);
        try {
            checkin.execute(_fileItem.getName(), _fileItem.getInputStream(), (int) _fileItem.getSize());
            final ImageInfo info = Imaging.getImageInfo(_fileItem.getInputStream(), _fileItem.getName());
            updateImage(_parameter, _imageInst, info);
        } catch (final IOException e) {
            throw new EFapsException(this.getClass(), "uploadImage", e, _parameter);
        }
    }


    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _imageInst    Instance the imgae will be check in to
     * @param _file         file to be checked in
     * @throws EFapsException on error
     */
    protected void uploadImage(final Parameter _parameter,
                               final Instance _imageInst,
                               final File _file)
        throws EFapsException
    {
        final Checkin checkin = new Checkin(_imageInst);
        try {
            final FileInputStream in = new FileInputStream(_file);
            checkin.execute(_file.getName(), in, ((Long) _file.length()).intValue());
            final ImageInfo info = Imaging.getImageInfo(_file);
            updateImage(_parameter, _imageInst, info);
        } catch (final IOException e) {
            throw new EFapsException(this.getClass(), "uploadImage", e, _parameter);
        }
    }


    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _imageInst    instance of the image to be update with the info
     * @param _info         ImageInfo
     * @throws EFapsException on error
     */
    protected void updateImage(final Parameter _parameter,
                               final Instance _imageInst,
                               final ImageInfo _info)
        throws EFapsException
    {
        final Update update = new Update(_imageInst);
        update.add(CIHumanResource.ImageAbstract.WidthPx, _info.getWidth());
        update.add(CIHumanResource.ImageAbstract.HeightPx, _info.getHeight());
        update.add(CIHumanResource.ImageAbstract.HeightDPI, _info.getPhysicalHeightDpi());
        update.add(CIHumanResource.ImageAbstract.WidthDPI, _info.getPhysicalWidthDpi());
        update.add(CIHumanResource.ImageAbstract.HeightInch, _info.getPhysicalHeightInch());
        update.add(CIHumanResource.ImageAbstract.WidthInch, _info.getPhysicalWidthInch());
        update.add(CIHumanResource.ImageAbstract.NumberOfImages, _info.getNumberOfImages());
        update.add(CIHumanResource.ImageAbstract.Format, _info.getFormatName());
        update.add(CIHumanResource.ImageAbstract.ColorType, _info.getColorType());
        update.execute();
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _type Type of connection
     * @param _imageInst Instance of the Image to be connected
     * @param _prodInst instance of the product to connect to
     * @return instance of newly create connection
     * @throws EFapsException on error
     */
    protected Instance connectImage(final Parameter _parameter,
                                    final Type _type,
                                    final Instance _prodInst,
                                    final Instance _imageInst)
        throws EFapsException
    {
        final Insert insert = new Insert(_type);
        insert.add(CIHumanResource.Employee2ImageAbstract.EmployeeAbstractLink, _prodInst.getId());
        insert.add(CIHumanResource.Employee2ImageAbstract.ImageAbstractLink, _imageInst.getId());
        insert.add(CIHumanResource.Employee2ImageAbstract.Caption, _parameter.getParameterValue("caption"));
        insert.execute();
        return insert.getInstance();
    }

    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _type Type    of image to be created
     * @param _origInst     Instance of the original file
     * @return instance of newly create file
     * @throws EFapsException on error
     */
    protected Instance createImage(final Parameter _parameter,
                                   final Type _type,
                                   final Instance _origInst)
        throws EFapsException
    {
        final Insert insert = new Insert(_type);
        insert.add(CIHumanResource.ImageAbstract.Description, _parameter.getParameterValue("description4Create"));
        if (_origInst != null && _type.isKindOf(CIHumanResource.ImageCopy.getType())) {
            insert.add(CIHumanResource.ImageCopy.OriginalAbstractLink, _origInst.getId());
        }
        insert.execute();
        return insert.getInstance();
    }
}
