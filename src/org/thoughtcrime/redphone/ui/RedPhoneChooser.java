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

package org.thoughtcrime.redphone.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import org.thoughtcrime.redphone.Constants;
import com.mannywilson.mannycalls.R;
import org.thoughtcrime.redphone.RedPhone;
import org.thoughtcrime.redphone.RedPhoneService;
import org.thoughtcrime.redphone.call.CallChooserCache;
import org.thoughtcrime.redphone.call.CallListener;

/**
 * A lightweight dialog for prompting the user to upgrade their outgoing call.
 *
 * @author Moxie Marlinspike
 *
 */
public class RedPhoneChooser extends FragmentActivity {

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    initializeResources();
  }

  private void initializeResources() {

    UpgradeCallDialogFragment dialogFragment = new UpgradeCallDialogFragment(getIntent().getStringExtra(Constants.REMOTE_NUMBER));
    dialogFragment.show(getSupportFragmentManager(), "upgrade");
  }
}
