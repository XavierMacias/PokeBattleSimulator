from typing import Dict, Iterator, List

import pandas as pd
from bs4 import BeautifulSoup


class TableItemsScraper:
    def __init__(self, html: str) -> None:
        self.soup = BeautifulSoup(html, 'html.parser')

    def scrap_per_class(self, column_classes: pd.Series) -> Iterator[pd.Series]:
        return (
            pd.Series(self.__scrap_column(column_class), name=column_class) 
            for column_class in column_classes
        )

    def __scrap_column(self, column_class: str) -> Iterator[str]:
        return (
            result.text 
            for result in self.soup.find_all(class_=column_class)
        )
