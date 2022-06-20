import pandas as pd


class ColumnNormalizer:
    @classmethod
    def normalize(cls, df: pd.DataFrame) -> pd.DataFrame:
        df_normalized = df \
            .pipe(cls.__normalize_column_names) \
            .pipe(cls.__normalize_column_contents)

        return df_normalized

    @classmethod
    def __normalize_column_contents(cls, df: pd.DataFrame) -> pd.DataFrame:
        df = df.drop('none', axis='columns')

        for stat_column in ['HP', 'Atk', 'Def', 'SpA', 'SpD', 'Spe', 'BST']:
            df[stat_column] = df[stat_column].str[len(stat_column):].astype(int)

        return df

    @classmethod
    def __normalize_column_names(cls, df: pd.DataFrame) -> pd.DataFrame:
        new_column_names = [
            'tier', 'name', 'none', 
            'hability_1', 'hability_2', 'HP', 
            'Atk', 'Def', 'SpA', 
            'SpD', 'Spe', 'BST'
        ]
        
        df_normalized_column_names = df.set_axis(new_column_names, axis='columns')

        return df_normalized_column_names
