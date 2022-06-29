from statistics import mode
import pandas as pd
from data_preprocessing.iterator_data_gather import IteratorDataGather
from page_iterators_builders.moves_page import MovesPage
from page_iterators_builders.pokedex_page import PokedexPage


class Application:
    @classmethod
    def transform_iterators(cls, iterator_pairs):
        dfs = [
            IteratorDataGather.generate_df_rows(s_keys, iterator)
            for s_keys, iterator in iterator_pairs
        ]

        return dfs

    @classmethod
    def execute(cls) -> None:
        data = [
            [MovesPage(), 'extracted_data_moves'], 
            [PokedexPage(), 'extracted_data_pokedex']
        ]

        for page, output_basename in data:
            for i, df in enumerate(page.dfs):                
                df.to_csv(f'{output_basename}-{i}.csv', index=False, mode='a+')

