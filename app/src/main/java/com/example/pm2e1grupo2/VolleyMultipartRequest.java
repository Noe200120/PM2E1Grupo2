package com.example.pm2e1grupo2;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final String boundary = "----PM2E1Grupo2" + System.currentTimeMillis();

    public VolleyMultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return new HashMap<>();
    }

    protected Map<String, DataPart> getByteData() {
        return new HashMap<>();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Campos de texto
            for (Map.Entry<String, String> entry : getParams().entrySet()) {
                writeTextPart(bos, entry.getKey(), entry.getValue());
            }

            // Archivos (imágenes)
            for (Map.Entry<String, DataPart> entry : getByteData().entrySet()) {
                writeFilePart(bos, entry.getKey(), entry.getValue());
            }

            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (Exception e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    private void writeTextPart(OutputStream os, String name, String value) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes());
        os.write((value + "\r\n").getBytes());
    }

    private void writeFilePart(OutputStream os, String name, DataPart data) throws IOException {
        os.write(("--" + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + data.fileName + "\"\r\n").getBytes());
        os.write(("Content-Type: " + data.type + "\r\n\r\n").getBytes());
        os.write(data.content);
        os.write("\r\n".getBytes());
    }

    public static class DataPart {
        public final String fileName;
        public final byte[] content;
        public final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }
    }
}
