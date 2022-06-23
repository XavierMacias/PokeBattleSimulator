from typing import Iterable, Iterator

import pandas as pd

from data_preprocessing.string_pattern_searching import StringPatternSearching


class TextIteratorParser:
    @classmethod
    def filtered_rows(cls, iterator_rows: Iterator[str]) -> Iterator[Iterable[pd.Series]]:
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
