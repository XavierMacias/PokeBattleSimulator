from typing import Iterator

import numpy as np
import pandas as pd


class TableReshaper:
    @classmethod
    def build_reshaped_table(cls, iterator_rows: Iterator[pd.Series]) -> pd.DataFrame:
        reshaped_rows = cls.__reshape_rows(iterator_rows)
        df_reshaped = pd.concat(reshaped_rows, axis='columns')

        return df_reshaped
        
    @classmethod
    def __reshape_rows(cls, iterator_rows: Iterator[pd.Series]) -> Iterator[pd.DataFrame]:
        iterable_rows = list(iterator_rows)
        
        rows_lengths = {s.name: len(s.index) for s in iterable_rows}
        minimum_distance = min(rows_lengths.values())

        for s in iterable_rows:
            normalized_shape = (minimum_distance, (len(s.index) // minimum_distance))
            reshaped_row = np.array(s.values).reshape(normalized_shape)

            yield pd.DataFrame(reshaped_row)
