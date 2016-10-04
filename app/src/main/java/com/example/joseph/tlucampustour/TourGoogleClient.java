package com.example.joseph.tlucampustour;


import android.app.Application;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;

/**
 * Created by Joseph on 10/4/2016.
 */

public class TourGoogleClient  implements Parcelable{


    private static GoogleApiClient myGoogleClient;
    private Context myContext;
    private GoogleApiClient.ConnectionCallbacks myConnCallback;
    private GoogleApiClient.OnConnectionFailedListener myConnFailed;

    public TourGoogleClient(Context context, GoogleApiClient.ConnectionCallbacks connCallBack,
                            GoogleApiClient.OnConnectionFailedListener connFailed)
    {
        myContext = context;
        myConnCallback = connCallBack;
        myConnFailed = connFailed;
    }

    protected TourGoogleClient(Parcel in) {
    }

    public static final Creator<TourGoogleClient> CREATOR = new Creator<TourGoogleClient>() {
        @Override
        public TourGoogleClient createFromParcel(Parcel in) {
            return new TourGoogleClient(in);
        }

        @Override
        public TourGoogleClient[] newArray(int size) {
            return new TourGoogleClient[size];
        }
    };

    public GoogleApiClient buildGoogleClient()
    {
        myGoogleClient = new GoogleApiClient.Builder(myContext)
                .addConnectionCallbacks(myConnCallback)
                .addOnConnectionFailedListener(myConnFailed)
                .addApi(LocationServices.API)
                .build();
        return myGoogleClient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
