package com.elanciers.vasantham_stores_ecomm;

import android.app.Application;
import android.content.Context;
import android.widget.PopupWindow;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.elanciers.vasantham_stores_ecomm.Common.DBController;
import com.elanciers.vasantham_stores_ecomm.Common.DatabaseManager;
import com.elanciers.vasantham_stores_ecomm.Common.Utils;
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase;

import org.json.JSONObject;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import android.util.Log;


public class AppController extends MultiDexApplication implements LifecycleObserver {

    private static Application instance;
    public static String username;
    public static String email;
    public static String phone;
    public static String imageurl;
    public static JSONObject languageobj;
    public static JSONObject Enlanguageobj;
    public static JSONObject Talanguageobj;
    //public static searchListiner searchlistiner;
    public static PopupWindow categoryPopWindow;
    //public static  LatLng currentLatlang;
    public static  String profilemode="E";
    public static final String save_userMobile="save_userMobile";
    public static boolean wasInForground;

    public static String getImageurl() {
        return imageurl;
    }

    public static void setImageurl(String imageurl) {
        AppController.imageurl = imageurl;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AppController.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        AppController.email = email;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        AppController.phone = phone;
    }

    public static final String TAG = AppController.class.getSimpleName();
    private static Context mContext;
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    public boolean reloadHomeScreen = false;
    public static boolean needProgressbar=true;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }



   /* private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        AppController app = (AppController) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(Utils.getVideoCacheDir(this))
                .build();
    }*/


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
        //Log.e("attachBaseContext","attachBaseContext called");
        //new Effects().init(this);
        MultiDex.install(this);
        mContext = this;
        if(new File(getCacheDir(),"language.txt").exists()){
            new ApiCall().readFromFile(new File(getCacheDir(),"language.txt"), new Utils(this).getLanguage());
        }

    }

    /*public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(),getSecureHttpConnection(this));
        }

        return mRequestQueue;
    }*/

    /*public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }*/

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.e("onCreate","onCreate called");
        instance = this;
        //new Effects().init(this);
        //MobileAds.initialize(this, getString(R.string.app_id));
        CardHistoryDatabase.Companion.getDatabase(this);
        DatabaseManager.Companion.initializeInstance(new DBController(this));
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        //ssl();

        //getSecureHttpConnection(this);

      /*  ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/robo_reg.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .setFontMapper(new FontMapper() {
                                    @Override
                                    public String map(String font) {
                                        return font;
                                    }
                                })
                                .build()))
                .build());*/

    }

    /*public HurlStack getSecureHttpConnection(Context context){

        HurlStack hurlStack = null;
        try{
            hurlStack = new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory(context));
                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }
            };

        }catch (Exception e){
            e.printStackTrace();
        }

        return hurlStack;
    }*/



    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            //Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            // Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }


    // Let's assume your server app is hosting inside a server machine
    // which has a server certificate in which "Issued to" is "localhost",for example.
    // Then, inside verify method you can verify "localhost".
    // If not, you can temporarily return true
    public HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //verify proper hostname with the certificate name
                /*HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                String hostName = AppConfig.base_url.replace("https://","").replace(":8001","");

                return hv.verify(hostName, session);*/

                return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
            }
        };
    }

    public static Context getAppContext() {
        return instance;
    }

    public static boolean isNeedProgressbar() {
        return needProgressbar;
    }

    public static void setNeedProgressbar(boolean needProgressbar) {
        AppController.needProgressbar = needProgressbar;
    }

    public static JSONObject getLanguageobj() {
        return languageobj;
    }

    public static JSONObject getEnLanguageobj() {
        return Enlanguageobj;
    }

    public static JSONObject getTaLanguageobj() {
        return Talanguageobj;
    }

    public static void setLanguageobj(JSONObject languageobj) {
        AppController.languageobj = languageobj;
    }

    public static void setEnLanguageobj(JSONObject languageobj) {
        AppController.Enlanguageobj = languageobj;
    }

    public static void setTaLanguageobj(JSONObject languageobj) {
        AppController.Talanguageobj = languageobj;
    }

    /*public static searchListiner getSearchlistiner() {
        return searchlistiner;
    }

    public static void setSearchlistiner(searchListiner searchlistiner) {
        AppController.searchlistiner = searchlistiner;
    }*/

    public static PopupWindow getCategoryPopWindow() {
        return categoryPopWindow;
    }

    public static void setCategoryPopWindow(PopupWindow categoryPopWindow) {
        AppController.categoryPopWindow = categoryPopWindow;
    }

    /*public static LatLng getCurrentLatlang() {
        return currentLatlang;
    }

    public static void setCurrentLatlang(LatLng currentLatlang) {
        AppController.currentLatlang = currentLatlang;
    }*/

    public static String getProfilemode() {
        return profilemode;
    }

    public static void setProfilemode(String profilemode) {
        AppController.profilemode = profilemode;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        wasInForground=true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        wasInForground =false;
    }
}
