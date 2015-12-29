package com.example.matias.tprof;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.matias.tprof.data.QuotesContract;
import com.example.matias.tprof.sync.AppSyncAdapter;


public class StockQuotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int STOCKS_LOADER = 0;

    private static final String[] STOCK_QUOTE_COLUMNS = {
            QuotesContract.StockEntry.TABLE_NAME + "." + QuotesContract.StockEntry._ID,
            QuotesContract.StockEntry.COLUMN_FULLNAME,
            QuotesContract.StockEntry.COLUMN_SYMBOL,
            QuotesContract.StockQuotesEntry.TABLE_NAME + "." + QuotesContract.StockQuotesEntry._ID,
            QuotesContract.StockQuotesEntry.COLUMN_LAST_TRADE_PRICE,
            QuotesContract.StockQuotesEntry.COLUMN_LAST_TRADE_DATE,
            QuotesContract.StockQuotesEntry.COLUMN_LAST_CHANGE,
            QuotesContract.StockQuotesEntry.COLUMN_LAST_CHANGE_PERCENTAGE,
            QuotesContract.StockQuotesEntry.COLUMN_CURRENCY,
    };

    static final int COL_STOCK_ID = 0;
    static final int COL_FULLNAME = 1;
    static final int COL_SYMBOL = 2;
    static final int COL_STOCK_QUOTE_ID = 3;
    static final int COL_LAST_PRICE = 4;
    static final int COL_LAST_TRADE_DATE = 5;
    static final int COL_LAST_CHANGE = 6;
    static final int COL_LAST_CHANGE_PERCENTAGE = 7;
    static final int COL_CURRENCY = 8;

    private StockQuotesAdapter mQuotesAdapter;

    public StockQuotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String sortOrder = QuotesContract.StockEntry.COLUMN_FULLNAME + " ASC";
        Uri stocksUri = QuotesContract.StockQuotesEntry.CONTENT_URI;

        Cursor cur = getActivity().getContentResolver().query(stocksUri,
                null, null, null, sortOrder);

        mQuotesAdapter = new StockQuotesAdapter(getActivity(), cur, 0);

        View rootView = inflater.inflate(R.layout.fragment_stock_quotes, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_stock_quotes);
        listView.setAdapter(mQuotesAdapter);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STOCKS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateQuotes();
    }

    private void updateQuotes() {
        AppSyncAdapter.syncImmediately(getActivity());
        //FetchStockDataTask fetchTestDataTask = new FetchStockDataTask(getContext());
        //fetchTestDataTask.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = QuotesContract.StockEntry.COLUMN_FULLNAME + " ASC";
        Uri stocksUri = QuotesContract.StockQuotesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                stocksUri,
                STOCK_QUOTE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mQuotesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuotesAdapter.swapCursor(null);
    }
}
