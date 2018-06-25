package kr.hs.dgsw.ahnt3.Networks;

import android.os.AsyncTask;

/**
 * Created by DH on 2018-04-10.
 */
//region HttpAsyncTask Class
public class HttpAsyncTask extends AsyncTask<String, String, String> {


    public HttpAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    NetworkHelper network;
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... data) {
        if(data[1] != null) {
            return network.POST(data[0], data[1], data[2]);
        }else
            return network.GET(data[0], data[2]);
    }

    @Override
    protected void onPostExecute(String postResult) {
        delegate.processFinish(postResult);
    }
}
//endregion