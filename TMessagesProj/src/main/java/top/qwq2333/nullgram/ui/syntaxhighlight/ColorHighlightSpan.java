/*
 * Copyright (C) 2019-2023 qwq233 <qwq233@qwq2333.top>
 * https://github.com/qwq233/Nullgram
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this software.
 *  If not, see
 * <https://www.gnu.org/licenses/>
 */

package top.qwq2333.nullgram.ui.syntaxhighlight;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Components.TextStyleSpan;

public class ColorHighlightSpan extends ReplacementSpan {

    private static final int radius = AndroidUtilities.dp(4);
    private static final int spacing = AndroidUtilities.dp(2);
    private static final Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int size = AndroidUtilities.dp(SharedConfig.fontSize - 1);
    private final int color;
    private final TextStyleSpan.TextStyleRun style;

    public ColorHighlightSpan(int color, TextStyleSpan.TextStyleRun run) {
        this.color = color;
        this.style = run;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       int start, int end,
                       @Nullable Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end) + spacing + size);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        var textPaint = (TextPaint) paint;
        var linkColor = textPaint.linkColor;
        var textColor = textPaint.getColor();
        if (style != null) {
            style.applyStyle(textPaint);
        }
        textPaint.setUnderlineText(linkColor == textColor);
        textPaint.setColor(linkColor);
        canvas.drawText(text, start, end, x, y, textPaint);

        colorPaint.setColor(color);
        var paddingTop = top + (bottom - top - size) / 2.0f;
        var paddingLeft = x + textPaint.measureText(text, start, end) + spacing;
        canvas.drawRoundRect(paddingLeft, paddingTop, paddingLeft + size, paddingTop + size, radius, radius, colorPaint);
    }
}
