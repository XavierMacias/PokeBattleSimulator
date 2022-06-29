from typing import Iterable, Iterator

import pandas as pd
from data_preprocessing.string_pattern_searching import StringPatternSearching
from data_scraping.table_items_scraper import TableItemsScraper
from data_scraping.webpage_content import WebpageContent

from page_iterators_builders.table_webpage import TableWebpage


class MovesPage(TableWebpage):
    def build_content_iterators(self):
        s_keys_text = [
            pd.Series(['name']),
            pd.Series(['power']),
            pd.Series(['accuracy']), 
            pd.Series(['PP']),
            pd.Series(['description']), 
        ]

        iterator_row_texts = TableItemsScraper.scrap_row_text(self._soup, 'result')
        iterator_row_texts = self.parse_text_row(iterator_row_texts)

        s_keys_images = [pd.Series(['type', 'category'])]
        iterator_row_images = TableItemsScraper.scrap_row_image_attribute(self._soup, 'alt')

        return [s_keys_text, iterator_row_texts], [s_keys_images, iterator_row_images]

    def load_page_content(self):
        return WebpageContent.load('https://dex.pokemonshowdown.com/moves/')

    def parse_text_row(self, iterator_rows: Iterator[str]) -> Iterator[Iterable[pd.Series]]:
        for text in iterator_rows:
            data = text.split('  ')
            if len(data) == 2:
                name = pd.Series([data[0]])

                power, other = data.pop(1).split('Accuracy')
                if power == ' ':
                    power = pd.Series([0.0])
                else:
                    power = StringPatternSearching.extract_numbers(power)
                other = 'Accuracy' + other

                other = other.split(' ')

                accuracy = StringPatternSearching.extract_numbers(other[0]) / 100

                if accuracy.empty:
                    accuracy = pd.Series([1.0])

                pp = StringPatternSearching.extract_numbers(other[1])
                description = pd.Series([' '.join(other[2:-1])])

                yield name, power, accuracy, pp, description
