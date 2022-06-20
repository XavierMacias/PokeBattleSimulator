from data_scraping.dynamic_content_loader import DynamicContentLoader
from data_scraping.pokemon_table_scraper import PokemonTableScraper
from data_scraping.web_driver import WebDriver


if __name__ == '__main__':
    web_driver = WebDriver.build()
    html = DynamicContentLoader.load_html('https://dex.pokemonshowdown.com/pokemon/', web_driver)

    df_table = PokemonTableScraper(html).df_table
    print(df_table)
