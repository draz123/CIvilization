Dane na temat żyzności gleby
=============================

* strona główna: 
  http://webarchive.iiasa.ac.at/Research/LUC/External-World-soil-database/HTML/index.html
* download: 
  http://webarchive.iiasa.ac.at/Research/LUC/External-World-soil-database/HTML/SoilQualityData.html?sb=11


## Zawartość

Na stronie znajdują się pliki zawierające mapy posczególnych parametrów terenu:


| plik     | opis                                                              |
| -------- | ----------------------------------------------------------------- |
| sq1.asc  | Nutrient availability                                             |
| sq2.asc  | Nutrient retention capacity                                       |
| sq3.asc  | Rooting conditions                                                |
| sq4.asc  | Oxygen availability to roots                                      |
| sq5.asc  | Excess salts                                                      |
| sq6.asc  | Toxicity                                                          |
| sq7.asc  | Workability (constraining field management)                       |

* szczegółowy opis parametrów: http://webarchive.iiasa.ac.at/Research/LUC/External-World-soil-database/HTML/SoilQuality.html?sb=10

## Format danych

Każdy plik składa się z:
  * nagłówka
    * liczba kolumn macierzy
    * liczba wierszy macierzy
    * długość geograficzna lewego dolnego rogu macierzy
    * szerokość geograficzna lewego dolnego rogu macierzy
    * wielkość komórki (w stopniach)
  * macierzy wartości parametru żyzności dla danej komórki (0-9), czyli np. wiersz 1080 będzie przedstawiał wartości parametru żyzności dla Równika


## Przykładowy fragment pliku

![data_example](http://oi67.tinypic.com/33oo1op.jpg)

## Propozycje

* ustalić rozmiar automatu na rozmiar komórki macierzy
* dodać do klasy automatu 7 atrybutów odzwierciedlających dane z 7 plików
* napisać parser, który dla każdego pliku:
  * usunie nagłówki
  * wytnie z pliku tylko basen M. Śródziemnego
  * zapisze dane do odpowiedniego atrybutu automatu
* obliczyć atrybut żyzności gleby jako średnia (ważona/arytmetyczna) z otrzymanych 7 atrybutów

## Inne

* czy do funkcji oceny potrzebujemy jeszcze nachylenia powierzchni i wysokości n.p.m.?
