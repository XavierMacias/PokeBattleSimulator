import pandas as pd
from data_preprocessing.preprocessed_row_builder import PreprocessedRowBuilder
from data_scraping.chrome_web_driver import ChromeWebDriver
from data_scraping.dynamic_content_loader import DynamicContentLoader
from data_scraping.table_items_scraper import TableItemsScraper


class Application:
    @classmethod
    def execute(cls) -> None:
        chrome_web_driver = ChromeWebDriver.build()
        html = DynamicContentLoader.load_html('https://dex.pokemonshowdown.com/pokemon/', chrome_web_driver)

        iterator_rows = TableItemsScraper.scrap_per_rows(html, 'result')
        iterator_df_rows = PreprocessedRowBuilder.build_preprocessed_rows(iterator_rows)

        types_iterator = TableItemsScraper.scrap_types(html)
        df_types = pd.concat(types_iterator)

        df_total = pd.concat(iterator_df_rows, axis='index')
        df_total = pd.concat([df_total, df_types], axis='columns')

        df_total = df_total[[
            'tier', 'name', 'type_1', 
            'type_2', 'hability_1', 'hability_2', 
            'hidden_hability', 'HP', 'Atk', 'Def', 
            'SpA', 'SpD', 'Spe', 'BST'
        ]]

        df_total.to_csv('extracted_data.csv', index=False)
