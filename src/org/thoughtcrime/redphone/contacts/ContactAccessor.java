/*
 * Copyright (C) 2011 Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.thoughtcrime.redphone.contacts;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;
import com.mannywilson.mannycalls.R;

import java.io.InputStream;

/**
 * Interface for accessing contacts.  Used to delegate between old (1.x) and
 * new (2.x+) Contacts interfaces, but we've finally dropped 1.6 support.
 *
 * @author Moxie Marlinspike
 *
 */

public class ContactAccessor {

  private static final ContactAccessor instance = new ContactAccessor();

  private static Bitmap defaultContactPhoto;

  private static final String[] PEOPLE_PROJECTION = {Phone.TYPE, Phone.DISPLAY_NAME,
                                                     Phone.CONTACT_ID, Phone.NUMBER, Phone._ID};

  private static final String PEOPLE_SELECTION  = "( " + Phone.DISPLAY_NAME + " NOT NULL )";

  private static final String PEOPLE_ORDER = "UPPER( " + Phone.DISPLAY_NAME + " ) ASC";

  private static final String FAVORITES_ORDER = Phone.TIMES_CONTACTED + " DESC LIMIT 20";

  public static ContactAccessor getInstance() {
    return instance;
  }

  private ContactAccessor() {}

  public CursorLoader getRegisteredContactsCursor(Context context) {
    return getRegisteredContactsCursor(context, Phone.CONTENT_URI);
  }

  public CursorLoader getRegisteredContactsCursor(Context context, String filter) {
    Uri uri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(filter));
    return getRegisteredContactsCursor(context, uri);
  }

  private CursorLoader getRegisteredContactsCursor(Context context, Uri uri) {
    return new RegisteredUserCursorLoader(context, uri, PEOPLE_PROJECTION,
                                          PEOPLE_SELECTION, PEOPLE_ORDER, Phone.NUMBER, true);
  }

  public CursorLoader getRegisteredFavoritesCursor(Context context) {
    return getRegisteredFavoritesCursor(context, Phone.CONTENT_URI);
  }

  public CursorLoader getRegisteredFavoritesCursor(Context context, String filter) {
    Uri uri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(filter));
    return getRegisteredFavoritesCursor(context, uri);
  }

  public CursorLoader getRegisteredFavoritesCursor(Context context, Uri uri) {
    return new RegisteredUserCursorLoader(context, uri, PEOPLE_PROJECTION,
                                          PEOPLE_SELECTION, FAVORITES_ORDER, Phone.NUMBER, true);
  }

  public Bitmap getPhoto(Context context, long rowId) {
    Uri photoLookupUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowId);
    return getPhoto(context, photoLookupUri);
  }

  public Bitmap getPhoto(Context context, Uri uri) {
    InputStream inputStream =
        ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);

    if (inputStream == null) return getDefaultContactPhoto(context);
    else                     return BitmapFactory.decodeStream(inputStream);
  }

  public Bitmap getDefaultContactPhoto(Context context) {
    synchronized (this) {
      if (defaultContactPhoto == null)
        defaultContactPhoto =  BitmapFactory.decodeResource(context.getResources(),
                              R.drawable.photoicon);
    }

    return defaultContactPhoto;
  }


  public class NumberData {
    public String number;
    public String type;

    @Override
    public String toString() {
      return type + ": " + number;
    }
  }
}
