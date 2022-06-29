from typing import Iterable, Iterator

import pandas as pd
from data_preprocessing.string_pattern_searching import StringPatternSearching
from data_scraping.table_items_scraper import TableItemsScraper
from data_scraping.webpage_content import WebpageContent

from page_iterators_builders.table_webpage import TableWebpage


class PokedexPage(TableWebpage):
    def build_content_iterators(self):
        s_keys_text = [
            pd.Series(['tier']), 
            pd.Series(['name']), 
            pd.Series(['hability_1', 'hability_2', 'hidden_hability']), 
            pd.Series(['HP', 'Atk', 'Def', 'SpA', 'SpD', 'Spe', 'BST'])
        ]

        iterator_row_texts = TableItemsScraper.scrap_row_text(self._soup, 'result')
        iterator_row_texts = self.parse_text_row(iterator_row_texts)

        s_keys_images = [pd.Series(['type_1', 'type_2'])]
        iterator_row_images = TableItemsScraper.scrap_row_image_attribute(self._soup, 'alt')

        return [s_keys_text, iterator_row_texts], [s_keys_images, iterator_row_images]

    def load_page_content(self):
        return WebpageContent.load('https://dex.pokemonshowdown.com/pokemon/')

    def parse_text_row(self, iterator_rows: Iterator[str]) -> Iterator[Iterable[pd.Series]]:
        for text in iterator_rows:
            data = text.split('  ')
            if len(data) == 3:
                tier = pd.Series([data[0]])
                name = pd.Series([data[1]])

                habilities, stats = data.pop(2).split('HP')
                habilities = StringPatternSearching.split_by_camelcase(habilities)

                stats = 'HP' + stats
                stats = StringPatternSearching.extract_numbers(stats)

                yield tier, name, habilities, stats
