package com.example.pm2e1grupo2;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final Response.ErrorListener mErrorListener;
    private final String boundary = "----PM2E1Boundary" + System.currentTimeMillis();
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        setShouldCache(false);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Map<String, String> params = getParams();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    writeTextPart(bos, entry.getKey(), entry.getValue());
                }
            }

            Map<String, DataPart> data = getByteData();
            if (data != null) {
                for (Map.Entry<String, DataPart> entry : data.entrySet()) {
                    writeFilePart(bos, entry.getKey(), entry.getValue());
                }
            }

            bos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private void writeTextPart(OutputStream os, String name, String value) throws IOException {
        os.write((twoHyphens + boundary + lineEnd).getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd).getBytes());
        os.write(("Content-Type: text/plain; charset=UTF-8" + lineEnd + lineEnd).getBytes());
        os.write(value.getBytes());
        os.write(lineEnd.getBytes());
    }

    private void writeFilePart(OutputStream os, String name, DataPart data) throws IOException {
        os.write((twoHyphens + boundary + lineEnd).getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + data.fileName + "\"" + lineEnd).getBytes());
        os.write(("Content-Type: " + data.type + lineEnd + lineEnd).getBytes());
        os.write(data.content);
        os.write(lineEnd.getBytes());
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = new String(response.data);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    protected abstract Map<String, String> getParams() throws AuthFailureError;
    protected abstract Map<String, DataPart> getByteData();

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
