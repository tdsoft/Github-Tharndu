package tdsolutions.com.gridlayoutlearning;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Admin on 1/6/2017.
 */

public class ColumnWeightSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final int[] columnWeights;

    ColumnWeightSpanSizeLookup(int[] columnWeights) {
        this.columnWeights = columnWeights;
    }

    @Override
    public int getSpanSize(int position) {
        return (columnWeights[position % columnWeights.length]);
    }

    int getTotalSpans() {
        int sum = 0;

        for (int weight : columnWeights) {
            sum += weight;
        }

        return (sum);
    }
}
