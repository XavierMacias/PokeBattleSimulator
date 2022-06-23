from typing import List
import pandas as pd
from bs4 import BeautifulSoup
from data_preprocessing.iterator_data_gather import IteratorDataGather
from data_preprocessing.text_iterator_parser import TextIteratorParser
from data_scraping.chrome_web_driver import ChromeWebDriver
from data_scraping.dynamic_content_loader import DynamicContentLoader
from data_scraping.table_items_scraper import TableItemsScraper


class Application:
    @classmethod
    def get_soup(cls):
        chrome_web_driver = ChromeWebDriver.build()
        html = DynamicContentLoader.load_html('https://dex.pokemonshowdown.com/pokemon/', chrome_web_driver)
        soup = BeautifulSoup(html, 'html.parser')

        return soup

    @classmethod
    def pair_keys_iterators(cls, soup: BeautifulSoup):    
        s_keys_text = [
            pd.Series(['tier']), 
            pd.Series(['name']), 
            pd.Series(['hability_1', 'hability_2', 'hidden_hability']), 
            pd.Series(['HP', 'Atk', 'Def', 'SpA', 'SpD', 'Spe', 'BST'])
        ]

        iterator_row_texts = TableItemsScraper.scrap_row_text(soup, 'result')
        iterator_row_texts = TextIteratorParser.filtered_rows(iterator_row_texts)

        s_keys_images = [pd.Series(['type_1', 'type_2'])]
        iterator_row_images = TableItemsScraper.scrap_row_image_attribute(soup, 'alt')

        return [s_keys_text, iterator_row_texts], [s_keys_images, iterator_row_images]

    @classmethod
    def transforme_iterator(cls, iterator_pairs):
        dfs = [
            pd.concat(IteratorDataGather.generate_df_rows(s_keys, iterator))
            for s_keys, iterator in iterator_pairs
        ]

        return dfs

    @classmethod
    def merge_all(cls, dfs: List[pd.DataFrame]) -> pd.DataFrame:
        df_total = pd.concat(dfs, axis='columns')

        return df_total

    @classmethod
    def execute(cls) -> None:
        soup = cls.get_soup()
        iterator_pairs = cls.pair_keys_iterators(soup)

        dfs = cls.transforme_iterator(iterator_pairs)
        df_total = cls.merge_all(dfs)

        df_total = df_total[[
            'tier', 'name', 'type_1', 
            'type_2', 'hability_1', 'hability_2', 
            'hidden_hability', 'HP', 'Atk', 'Def', 
            'SpA', 'SpD', 'Spe', 'BST'
        ]]

        df_total.to_csv('extracted_data.csv', index=False)
