import itertools
from collections import OrderedDict
from typing import Iterator

import pandas as pd
from bs4 import BeautifulSoup


class TableItemsScraper:
    @classmethod
    def scrap_per_rows(cls, html: str, class_row_items: str) -> Iterator[str]:
        soup = BeautifulSoup(html, 'html.parser')

        return (result.text for result in soup.find_all(class_=class_row_items))

    @classmethod
    def scrap_types(cls, html: str) -> Iterator[pd.DataFrame]:
        soup = BeautifulSoup(html, 'html.parser')

        for result in soup.find_all(class_='col typecol'):
            images = result.find_all('img')
            found_types = OrderedDict.fromkeys((image.attrs['alt'] for image in images))
            
            types_desc = ['type_1', 'type_2']
            types_mapping = dict(itertools.zip_longest(types_desc, found_types, fillvalue=None))

            df = pd.DataFrame.from_dict(types_mapping, orient='index').T

            yield df
