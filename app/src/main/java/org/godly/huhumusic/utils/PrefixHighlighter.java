/*
 * researching purpose(C) 2011 The Android Open Source Project Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.godly.huhumusic.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Highlights the text in a text field.
 */
public class PrefixHighlighter {

	/* Color used when highlighting the prefixes */
	private int mPrefixHighlightColor;

	private ForegroundColorSpan mPrefixColorSpan;

	/**
	 *
	 */
	public PrefixHighlighter(Context context) {
		mPrefixHighlightColor = PreferenceUtils.getInstance(context).getDefaultThemeColor();
	}

	/**
	 * Sets the text on the given {@link TextView}, highlighting the word that
	 * matches the given prefix.
	 *
	 * @param view   The {@link TextView} on which to set the text
	 * @param text   The string to use as the text
	 * @param prefix The prefix to look for
	 */
	public void setText(TextView view, String text, char[] prefix) {
		if (view == null || TextUtils.isEmpty(text) || prefix == null || prefix.length == 0) {
			return;
		}
		view.setText(apply(text, prefix));
	}

	/**
	 * Returns a {@link CharSequence} which highlights the given prefix if found
	 * in the given text.
	 *
	 * @param text   the text to which to apply the highlight
	 * @param prefix the prefix to look for
	 */
	public CharSequence apply(CharSequence text, char[] prefix) {
		int mIndex = indexOfWordPrefix(text, prefix);
		if (mIndex != -1) {
			if (mPrefixColorSpan == null) {
				mPrefixColorSpan = new ForegroundColorSpan(mPrefixHighlightColor);
			}
			SpannableString mResult = new SpannableString(text);
			mResult.setSpan(mPrefixColorSpan, mIndex, mIndex + prefix.length, 0);
			return mResult;
		} else {
			return text;
		}
	}

	/**
	 * Finds the index of the first word that starts with the given prefix. If
	 * not found, returns -1.
	 *
	 * @param text   the text in which to search for the prefix
	 * @param prefix the text to find, in upper case letters
	 */
	private int indexOfWordPrefix(CharSequence text, char[] prefix) {
		if (TextUtils.isEmpty(text) || prefix == null) {
			return -1;
		}

		int mTextLength = text.length();
		int mPrefixLength = prefix.length;

		if (mPrefixLength == 0 || mTextLength < mPrefixLength) {
			return -1;
		}

		int i = 0;
		while (i < mTextLength) {
			/* Skip non-word characters */
			while (i < mTextLength && !Character.isLetterOrDigit(text.charAt(i))) {
				i++;
			}

			if (i + mPrefixLength > mTextLength) {
				return -1;
			}

			/* Compare the prefixes */
			int j;
			for (j = 0; j < mPrefixLength; j++) {
				if (Character.toUpperCase(text.charAt(i + j)) != prefix[j]) {
					break;
				}
			}
			if (j == mPrefixLength) {
				return i;
			}

			/* Skip this word */
			while (i < mTextLength && Character.isLetterOrDigit(text.charAt(i))) {
				i++;
			}
		}
		return -1;
	}
}