package tdsolutions.com.tempprojectspannable;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/11/2017.
 */

public class SimpleSpanBuilder {
    public static final int FORMATTING_STYLE_DARK_BOLD = 1;
    public static final int FORMATTING_STYLE_DARK_BOLD_SMALL = 2;
    public static final int FORMATTING_STYLE_DIM_ITALIC_LIGHT = 3;
    public static final int FORMATTING_STYLE_DIM_ITALIC_LIGHT_SMALL = 4;
    private List<SpanSection> spanSections;
    private StringBuilder stringBuilder;
    RelativeSizeSpan relativeSmallSpan;
    private Context context;
    public SimpleSpanBuilder(Context context) {
        this.context = context;
        stringBuilder = new StringBuilder();
        spanSections = new ArrayList<>();
        relativeSmallSpan = new RelativeSizeSpan(0.8f);
    }

    public SimpleSpanBuilder append(String text, int formattingStyle) {
        spanSections.add(new SpanSection(text, stringBuilder.length(), formattingStyle));
        stringBuilder.append(text);
        return this;
    }

    public SpannableStringBuilder build() {
        SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuilder.toString());
        for (SpanSection section : spanSections) {
            section.apply(ssb);
        }
        return ssb;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private class SpanSection {
        private final String text;
        private final int startIndex;
        private final int formattingStyle;

        public SpanSection(String text, int startIndex, int formattingStyle) {
            this.formattingStyle = formattingStyle;
            this.text = text;
            this.startIndex = startIndex;
        }

        public void apply(SpannableStringBuilder spanStringBuilder) {
            if (spanStringBuilder == null) return;
            switch (formattingStyle) {
                case FORMATTING_STYLE_DARK_BOLD:
                    ForegroundColorSpan boldColorSpan =
                            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.Green));
                    spanStringBuilder.setSpan(boldColorSpan, startIndex, startIndex + text.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case FORMATTING_STYLE_DARK_BOLD_SMALL:
                    ForegroundColorSpan testColorSpan1 =
                            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.Black));

                    spanStringBuilder.setSpan(testColorSpan1, startIndex, startIndex + text.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case FORMATTING_STYLE_DIM_ITALIC_LIGHT:
                    ForegroundColorSpan dimColorSpan =
                            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.Blue));
                    spanStringBuilder.setSpan(dimColorSpan, startIndex, startIndex + text.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case FORMATTING_STYLE_DIM_ITALIC_LIGHT_SMALL:
                    ForegroundColorSpan testColorSpan2 =
                            new ForegroundColorSpan(ContextCompat.getColor(context, R.color.Red));
                    spanStringBuilder.setSpan(testColorSpan2, startIndex, startIndex + text.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
            }
        }
    }
}
