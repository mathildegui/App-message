package com.mathilde.appmessage.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.data.Blob;

import java.io.ByteArrayOutputStream;

/**
 * @author mathilde on 22/02/16.
 */
public class BitmapConverter extends TypeConverter<Blob, Bitmap> {
    @Override
    public Blob getDBValue(Bitmap model) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        model.compress(Bitmap.CompressFormat.PNG, 0, baos);

        return new Blob(baos.toByteArray());
    }

    @Override
    public Bitmap getModelValue(Blob data) {
        byte[] blob = data.getBlob();
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }
}
