/**
 * Copyright 2011 multibit.org
 *
 * Licensed under the MIT license (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://opensource.org/licenses/mit-license.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.multibit.viewsystem.swing.view.ticker;

import javax.swing.table.AbstractTableModel;

import org.multibit.controller.MultiBitController;
import org.multibit.model.exchange.ExchangeData;
import org.multibit.model.exchange.ExchangeModel;

/**
 * Table model for ticker.
 * 
 * @author jim
 * 
 */
public class TickerTableModel extends AbstractTableModel {

    public static final String TICKER_COLUMN_NONE = "none";
    public static final String TICKER_COLUMN_CURRENCY = "currency";
    public static final String TICKER_COLUMN_LAST_PRICE = "lastPrice";
    public static final String TICKER_COLUMN_BID = "bid";
    public static final String TICKER_COLUMN_ASK = "ask";
    public static final String TICKER_COLUMN_EXCHANGE = "exchange";

    private static final int MAX_NUMBER_OF_COLUMNS = 5;

    public static final String DEFAULT_COLUMNS_TO_SHOW = "currency lastPrice exchange";
    public static final String DEFAULT_CURRENCY = "USD";

    private static final long serialVersionUID = -775886012854496208L;

    /**
     * The exchange data for each row
     */
    private ExchangeData exchangeData1;
    private ExchangeData exchangeData2;

    private boolean showSecondRow;

    private String exchange1;
    private String currency1;

    private String exchange2;
    private String currency2;

    /**
     * the MultiBit model
     */

    private MultiBitController controller;

    private boolean showCurrency;
    private boolean showLastPrice;
    private boolean showBid;
    private boolean showAsk;
    private boolean showExchange;

    private String[] columnVariables = new String[MAX_NUMBER_OF_COLUMNS];
    private String[] tempColumns = new String[MAX_NUMBER_OF_COLUMNS];
    private int numberOfColumns;

    public TickerTableModel(MultiBitController controller) {
        this.controller = controller;

        String tickerColumnsToShow = controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_COLUMNS_TO_SHOW);

        if (tickerColumnsToShow == null || tickerColumnsToShow.equals("")) {
            tickerColumnsToShow = DEFAULT_COLUMNS_TO_SHOW;
        }
        showCurrency = tickerColumnsToShow.indexOf(TICKER_COLUMN_CURRENCY) > -1;
        showLastPrice = tickerColumnsToShow.indexOf(TICKER_COLUMN_LAST_PRICE) > -1;
        showBid = tickerColumnsToShow.indexOf(TICKER_COLUMN_BID) > -1;
        showAsk = tickerColumnsToShow.indexOf(TICKER_COLUMN_ASK) > -1;
        showExchange = tickerColumnsToShow.indexOf(TICKER_COLUMN_EXCHANGE) > -1;

        numberOfColumns = 0;
        if (showExchange) {
        	tempColumns[numberOfColumns] = TICKER_COLUMN_EXCHANGE;
        	numberOfColumns++;
        }
        if (showCurrency) {
            tempColumns[numberOfColumns] = TICKER_COLUMN_CURRENCY;
            numberOfColumns++;
        }
        if (showLastPrice) {
            tempColumns[numberOfColumns] = TICKER_COLUMN_LAST_PRICE;
            numberOfColumns++;
        }
        if (showBid) {
            tempColumns[numberOfColumns] = TICKER_COLUMN_BID;
            numberOfColumns++;
        }
        if (showAsk) {
            tempColumns[numberOfColumns] = TICKER_COLUMN_ASK;
            numberOfColumns++;
        }

        columnVariables = new String[numberOfColumns];
        for (int i = 0; i < numberOfColumns; i++) {
            columnVariables[i] = tempColumns[i];
        }

        showSecondRow = Boolean.TRUE.toString().equals(
                controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_SHOW_SECOND_ROW));

        exchange1 = controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_FIRST_ROW_EXCHANGE);
        if (exchange1 == null || "".equals(exchange1) || "null".equals(exchange1)) {
            exchange1 = ExchangeData.DEFAULT_EXCHANGE;
        }

        currency1 = controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_FIRST_ROW_CURRENCY);
        if (currency1 == null || "".equals(currency1) || "null".equals(currency1)) {
            currency1 = ExchangeData.DEFAULT_CURRENCY;
        }

        exchange2 = controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_SECOND_ROW_EXCHANGE);
        if (exchange2 == null || "".equals(exchange2) || "null".equals(exchange2)) {
            exchange2 = ExchangeData.DEFAULT_EXCHANGE;
        }

        currency2 = controller.getCoreModel().getUserPreference(ExchangeModel.TICKER_SECOND_ROW_CURRENCY);
        if (currency2 == null || "".equals(currency2) || "null".equals(currency2)) {
            currency2 = ExchangeData.DEFAULT_CURRENCY;
        }
        
        exchangeData1 = this.controller.getExchangeModel().getExchangeData1();
        exchangeData2 = this.controller.getExchangeModel().getExchangeData2();
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    public int getRowCount() {
        if (showSecondRow) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public String getColumnName(int column) {
        return controller.getLocaliser().getString("tickerTableModel." + columnVariables[column]);
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < 0 && row >= getRowCount()) {
            return null;
        }

        String exchange;
        String currency;
        ExchangeData exchangeData;
        if (row == 0) {
            exchange = exchange1;
            currency = currency1;
            exchangeData = exchangeData1;
        } else {
            exchange = exchange2;
            currency = currency2;
            exchangeData = exchangeData2;
        }

        String variable = columnVariables[column];

        if (TICKER_COLUMN_CURRENCY.equals(variable)) {
            // currency
            return currency;
        } else if (TICKER_COLUMN_LAST_PRICE.equals(variable)) {
            // rate
            if (exchangeData.getLastPrice(currency) == null) {
                return " ";
            } else {
                return controller.getLocaliser().bigMoneyValueToString(exchangeData.getLastPrice(currency));
            }
        } else if (TICKER_COLUMN_BID.equals(variable)) {
            // bid
            if (exchangeData.getLastBid(currency) == null) {
                return " ";
            } else {
                return controller.getLocaliser().bigMoneyValueToString(exchangeData.getLastBid(currency));
            }
        } else if (TICKER_COLUMN_ASK.equals(variable)) {
            // ask
            if (exchangeData.getLastAsk(currency) == null) {
                return " ";
            } else {
                 return controller.getLocaliser().bigMoneyValueToString(exchangeData.getLastAsk(currency));
            }
        } else if (TICKER_COLUMN_EXCHANGE.equals(variable)) {
            // exchange
            return exchange;
        } else {
            // do not know
            return "";
        }
    }

    /**
     * table model is read only
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        throw new UnsupportedOperationException();
    }

    public String[] getColumnVariables() {
        return columnVariables;
    }
}
