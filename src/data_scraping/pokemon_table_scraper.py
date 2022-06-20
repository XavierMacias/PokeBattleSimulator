from typing import Dict, Iterator, List

import pandas as pd
from bs4 import BeautifulSoup


class PokemonTableScraper:
    def __init__(self, html: str) -> None:
        self.soup = BeautifulSoup(html, 'html.parser')

        bijective_columns = {
            'tier': 'col numcol',
            'name': 'col pokemonnamecol',
            'BST': 'col bstcol'
        }

        self.df_table = pd.concat(self.generate_columns(bijective_columns), axis='columns')

    def scrap_column(self, column_class: str) -> Iterator[str]:
        results = self.soup.find_all(class_=column_class)

        for r in results:
            yield r.text

    def __is_stat_data(self, data: List[str]) -> bool:
        stats_preffixes = ('Atk', 'Def', 'SpA', 'SpD', 'Spe', 'BST')

        stat_data = all([
            any([stat in element for stat in stats_preffixes])
            for element in data
        ])

        return stat_data

    def generate_columns(self, columns: Dict[str, str]) -> Iterator[pd.Series]:
        for column_name, row_class in columns.items():
            data = list(self.scrap_column(row_class))

            if self.__is_stat_data(data):
                data = [element[3:] for element in data]

            s = pd.Series(data, name=column_name)

            yield s
