from abc import ABC, abstractmethod
from typing import Iterable, Iterator

import pandas as pd
from bs4 import BeautifulSoup
from data_preprocessing.iterator_data_gather import IteratorDataGather


class TableWebpage(ABC):
    def __init__(self) -> None:
        super().__init__()

        self._soup = self.load_page_content()
        self.content_iterators = self.build_content_iterators()
        self.dfs = self.transform_iterators()

    def transform_iterators(self) -> Iterable[pd.DataFrame]:
        dfs = [
            IteratorDataGather.generate_df_rows(s_keys, iterator)
            for s_keys, iterator in self.content_iterators
        ]

        return dfs

    @abstractmethod
    def build_content_iterators(self) -> Iterable[Iterator]:
        pass

    @abstractmethod
    def load_page_content(self) -> BeautifulSoup:
        pass

    @abstractmethod
    def parse_text_row(self, iterator_rows: Iterator[str]) -> Iterator[Iterable[pd.Series]]:
        pass
