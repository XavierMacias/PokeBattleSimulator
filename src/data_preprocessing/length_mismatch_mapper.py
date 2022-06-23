import itertools
import pandas as pd


class LengthMismatchMapper:
    @classmethod
    def map_to_df(cls, s_keys: pd.Series, s_values: pd.Series) -> pd.DataFrame:
        mismatched_zipping = itertools.zip_longest(s_keys, s_values, fillvalue=None)
        mismatched_mapping = dict(mismatched_zipping)

        df = pd.DataFrame.from_dict(mismatched_mapping, orient='index').T.convert_dtypes()

        return df
