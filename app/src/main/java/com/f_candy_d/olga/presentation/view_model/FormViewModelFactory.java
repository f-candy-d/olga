package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daichi on 9/13/17.
 */

final public class FormViewModelFactory {

    public enum Model implements Parcelable {
        EVENT_FORM(0);

        private final int mId;

        Model(int id) {
            mId = id;
        }

        int id() {
            return mId;
        }

        public static Model from(int id) {
            Model[] models = values();
            for (Model model : models) {
                if (model.id() == id) {
                    return model;
                }
            }
            return null;
        }

        /**
         * region; Parcelable implementation
         */

        public static final Parcelable.Creator<Model> CREATOR = new Creator<Model>() {
            @Override
            public Model createFromParcel(Parcel parcel) {
                return Model.from(parcel);
            }

            @Override
            public Model[] newArray(int size) {
                return new Model[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(this.id());
        }

        static Model from(Parcel in) {
            return from(in.readInt());
        }
    }

    public static FormViewModel create(
            Model model,
            Context context,
            FormViewModel.RequestReplyListener listener,
            long contentId) {

        switch (model) {
            case EVENT_FORM:
                return new EventFormViewModel(context, listener, contentId);

            default:
                return null;
        }
    }
}
