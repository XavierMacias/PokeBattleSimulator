import pandas as pd
from data_preprocessing.column_normalizer import ColumnNormalizer
from data_preprocessing.table_reshaper import TableReshaper
from data_scraping.dynamic_content_loader import DynamicContentLoader
from data_scraping.table_items_scraper import TableItemsScraper
from data_scraping.chrome_web_driver import ChromeWebDriver


class Application:
    @classmethod
    def execute(cls) -> None:
        chrome_web_driver = ChromeWebDriver.build()
        html = DynamicContentLoader.load_html('https://dex.pokemonshowdown.com/pokemon/', chrome_web_driver)

        classes_table_columns = pd.Series([
            'col numcol', 'col pokemonnamecol', 'col typecol', 'col abilitycol', 'col statcol', 'col bstcol'
        ])

        iterator_rows = TableItemsScraper(html).scrap_per_class(classes_table_columns)
        df_reshaped_table = TableReshaper.build_reshaped_table(iterator_rows)
        df_normalized_data = ColumnNormalizer.normalize(df_reshaped_table)

        df_normalized_data.to_csv('extracted_data.csv', index=False)
