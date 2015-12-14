package fertility_data;

import java.io.*;
import java.util.regex.*;

/**
 * A class which reads an ESRI ASCII raster file into a Raster
 * Originally by dmrust, cut to our needs
 * @author dmrust
 *
 */
public class RasterReaderMod
{
	String noData = RasterMod.DEFAULT_NODATA;
	Pattern header = Pattern.compile( "^(\\w+)\\s+(-?\\d+(.\\d+)?)");
	
	public static void main( String[] args ) throws IOException
	{
		RasterReaderMod rt = new RasterReaderMod();
		rt.readRaster( "data/test.asc" );
	}
	
	/**
	 * The most useful method - reads a raster file, and returns a Raster object.
	 * 
	 * Throws standard IOExceptions associated with opening and reading files, and
	 * RuntimeExceptions if there are problems with the file format
	 * @param filename
	 * @return the Raster object read in from the file
	 * @throws IOException
	 */
	public RasterMod readRaster( String filename ) throws IOException, RuntimeException
	{
		RasterMod raster = new RasterMod();
		BufferedReader input = new BufferedReader( new FileReader( filename ) );
		while( input.ready() )
		{
			String line = input.readLine();
			Matcher headMatch = header.matcher( line );
			//Match all the heads
			if( headMatch.matches() )
			{
				String head = headMatch.group( 1 );
				String value = headMatch.group( 2 );
				if( head.equalsIgnoreCase( "nrows" ) )
					raster.rows = Integer.parseInt( value );
				else if ( head.equalsIgnoreCase( "ncols" ) )
					raster.cols = Integer.parseInt( value );
				else if ( head.equalsIgnoreCase( "xllcorner" ) )
					raster.xll = Double.parseDouble( value );
				else if ( head.equalsIgnoreCase( "yllcorner" ) )
					raster.yll = Double.parseDouble( value );
				else if ( head.equalsIgnoreCase( "NODATA_value" ) )
					raster.NDATA = value;
				else if ( head.equals( "cellsize" ) )
					raster.cellsize = Double.parseDouble( value );
				else
					System.out.println( "Unknown setting: " + line );
			}
			else if( line.matches( "^\\s-?\\d+.*" )) //the differences come from the fact rows start with single space char in our files (no space in the library's author test files                                     
			{
				//System.out.println( "Processing data section");
				//Check that data is set up!
				//Start processing numbers!
				int row = 0;
				double[][] data = new double[raster.rows][];
				while( true )
				{
					//System.out.println( "Got data row: " + line );
					String[] inData = line.split( "\\s+" );
					double[] numData = new double[raster.cols];
					if( inData.length-1 != numData.length ) throw new RuntimeException( "Wrong number of columns: Expected " + 
							raster.cols + " got " + (inData.length-1) + " for line \n" + line );
					for( int col = 0; col < raster.cols; col ++ )
					{
						if( inData[col+1].equals( noData )) numData[col] = Double.NaN;
						else numData[col] = Double.parseDouble( inData[col+1] );
					}
					data[row] = numData;
					//Ugly backward input structure...
					if( input.ready() ) line = input.readLine();
					else break;
					row++;
				}
				if( row != raster.rows - 1)
					throw new RuntimeException( "Wrong number of rows: expected " + raster.rows + " got " + (row+1) );
				raster.data = data;
			}
			else
			{
				if( line.length() >= 0 && ! line.matches( "^\\s*$" ))
					System.out.println( "Unknown line: " + line);
			}
		}
		return raster;
	}
}
