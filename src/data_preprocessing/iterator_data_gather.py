from typing import Iterable

import pandas as pd

from data_preprocessing.length_mismatch_mapper import LengthMismatchMapper


class IteratorDataGather:
    @classmethod
    def generate_df_rows(cls, columns: Iterable[pd.Series], data: Iterable[pd.Series]) -> pd.DataFrame:
        iterator_df_rows = (cls.__group_row_data(columns, row) for row in data)
        df = pd.concat(iterator_df_rows)

        return df

    @classmethod
    def __group_row_data(cls, columns: Iterable[pd.Series], data: Iterable[pd.Series]) -> pd.DataFrame:
        dfs_row_groups = map(LengthMismatchMapper.map_to_df, columns, data)
        df_row = pd.concat(dfs_row_groups, axis='columns')

        return df_row