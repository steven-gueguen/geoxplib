//
//  GeoXP Lib, library for efficient geo data manipulation
//
//  Copyright (C) 1999-2016  Mathias Herberts
//
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Affero General Public License as
//  published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version and under the terms
//  of the GeoXP License Exception.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Affero General Public License for more details.
//
//  You should have received a copy of the GNU Affero General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package com.geoxp.geo;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class HHCodeHelperTestCase extends TestCase {
  
  @Test
  public void test() {
    System.out.println(HHCodeHelper.getLatLon(0xaaaaaaaaaaaaaaaaL, 32)[0]);
    System.out.println(HHCodeHelper.getLatLon(0xaaaaaaaaaaaaaaaaL, 32)[1]);
    System.out.println(HHCodeHelper.getLatLon(0x8888888888888888L, 32)[0]);
    System.out.println(HHCodeHelper.getLatLon(0x2222222222222222L, 32)[0]);
    System.out.println(HHCodeHelper.getLatLon(0x0145014501450145L, 32)[0]);
    System.out.println(HHCodeHelper.getLatLon(0x0000111144445555L, 32)[1]);
  }
  
  @Test
  public void testGetHHCodeValue() {    
    assertEquals(0xb570707070707070L, HHCodeHelper.getHHCodeValue(48.0, -4.5));
    assertEquals(0xc000000000000000L, HHCodeHelper.getHHCodeValue(0.0, 0.0));
    assertEquals(0x0000000000000000L, HHCodeHelper.getHHCodeValue(-90.0, -180.0));
    
    //
    // Test wrap around
    //
    
    //
    // Longitude wrap
    //
    
    Assert.assertEquals(HHCodeHelper.getHHCodeValue(0,-179), HHCodeHelper.getHHCodeValue(0,181.0));
    Assert.assertEquals(HHCodeHelper.getHHCodeValue(0,179), HHCodeHelper.getHHCodeValue(0,-181.0));
  
    //
    // Latitude wrap
    //
    
    Assert.assertEquals(HHCodeHelper.getHHCodeValue(89.0, -1.0), HHCodeHelper.getHHCodeValue(91.0, 179.0));
    
    assertEquals(0xeaaaaaaaaaaaaaaaL, HHCodeHelper.getHHCodeValue(90, 180));
    assertEquals(HHCodeHelper.getHHCodeValue(89.0, 1.0), HHCodeHelper.getHHCodeValue(91.0, 181.0));
  }
  
  @Test
  public void testSplitHHCode() {
    long[] coords = HHCodeHelper.splitHHCode(0xc000000000000000L, 32);
    
    assertEquals(0x80000000L, coords[0]);
    assertEquals(0x80000000L, coords[1]);

    coords = HHCodeHelper.splitHHCode(0xffffffffffffffffL, 16);

    assertEquals(0xffff0000L, coords[0]);
    assertEquals(0xffff0000L, coords[1]);
    
    coords = HHCodeHelper.splitHHCode(0xe036a70a028aa0aaL, 10);
    Assert.assertEquals(2248146944L, coords[1]);

    long hhcode = HHCodeHelper.buildHHCode(0x7fffffffL, 0x7ce09800L, 32);
    Assert.assertEquals(-4.190951585769653E-8, HHCodeHelper.getLatLon(hhcode, 32)[0], 0.0000000001);
  }
  
  @Test
  public void testAbove() {
    
    //
    // Test even levels for cell 0
    //
    
    assertEquals(0x0000000000000002L, HHCodeHelper.northHHCode(0L, 32));
    assertEquals(0x0000000000000020L, HHCodeHelper.northHHCode(0L, 30));
    assertEquals(0x0000000000000200L, HHCodeHelper.northHHCode(0L, 28));
    assertEquals(0x0000000000002000L, HHCodeHelper.northHHCode(0L, 26));
    assertEquals(0x0000000000020000L, HHCodeHelper.northHHCode(0L, 24));
    assertEquals(0x0000000000200000L, HHCodeHelper.northHHCode(0L, 22));
    assertEquals(0x0000000002000000L, HHCodeHelper.northHHCode(0L, 20));
    assertEquals(0x0000000020000000L, HHCodeHelper.northHHCode(0L, 18));
    assertEquals(0x0000000200000000L, HHCodeHelper.northHHCode(0L, 16));
    assertEquals(0x0000002000000000L, HHCodeHelper.northHHCode(0L, 14));
    assertEquals(0x0000020000000000L, HHCodeHelper.northHHCode(0L, 12));
    assertEquals(0x0000200000000000L, HHCodeHelper.northHHCode(0L, 10));
    assertEquals(0x0002000000000000L, HHCodeHelper.northHHCode(0L, 8));
    assertEquals(0x0020000000000000L, HHCodeHelper.northHHCode(0L, 6));
    assertEquals(0x0200000000000000L, HHCodeHelper.northHHCode(0L, 4));
    assertEquals(0x2000000000000000L, HHCodeHelper.northHHCode(0L, 2));
    
    //
    // Test above cell at level 32 for cells 1-f
    //
    
    assertEquals(0x0000000000000003L, HHCodeHelper.northHHCode(1L, 32));
    assertEquals(0x0000000000000006L, HHCodeHelper.northHHCode(4L, 32));
    assertEquals(0x0000000000000007L, HHCodeHelper.northHHCode(5L, 32));
    assertEquals(0x0000000000000008L, HHCodeHelper.northHHCode(2L, 32));
    assertEquals(0x0000000000000009L, HHCodeHelper.northHHCode(3L, 32));
    assertEquals(0x000000000000000cL, HHCodeHelper.northHHCode(6L, 32));
    assertEquals(0x000000000000000dL, HHCodeHelper.northHHCode(7L, 32));
    assertEquals(0x000000000000000aL, HHCodeHelper.northHHCode(8L, 32));
    assertEquals(0x000000000000000bL, HHCodeHelper.northHHCode(9L, 32));
    assertEquals(0x000000000000000eL, HHCodeHelper.northHHCode(0xcL, 32));
    assertEquals(0x000000000000000fL, HHCodeHelper.northHHCode(0xdL, 32));
    assertEquals(0x0000000000000020L, HHCodeHelper.northHHCode(0xaL, 32));
    assertEquals(0x0000000000000021L, HHCodeHelper.northHHCode(0xbL, 32));
    assertEquals(0x0000000000000024L, HHCodeHelper.northHHCode(0xeL, 32));
    assertEquals(0x0000000000000025L, HHCodeHelper.northHHCode(0xfL, 32));

    
    //
    // Test wrap around
    //
    
    assertEquals(0xeaaaaaaaaaaaaaaaL, HHCodeHelper.northHHCode(0xaaaaaaaaaaaaaaaaL, 32));
    assertEquals(0xfbbbbbbbbbbbbbbbL, HHCodeHelper.northHHCode(0xbbbbbbbbbbbbbbbbL, 32));
    assertEquals(0xaeeeeeeeeeeeeeeeL, HHCodeHelper.northHHCode(0xeeeeeeeeeeeeeeeeL, 32));
    assertEquals(0xbfffffffffffffffL, HHCodeHelper.northHHCode(0xffffffffffffffffL, 32));
  }
  
  @Test
  public void testBelow() {
    
    assertEquals(0x0000000000000000L, HHCodeHelper.southHHCode(0x0000000000000002L, 32));
    assertEquals(0x0000000000000001L, HHCodeHelper.southHHCode(0x0000000000000003L, 32));
    assertEquals(0x0000000000000004L, HHCodeHelper.southHHCode(0x0000000000000006L, 32));
    assertEquals(0x0000000000000005L, HHCodeHelper.southHHCode(0x0000000000000007L, 32));
    assertEquals(0x0000000000000002L, HHCodeHelper.southHHCode(0x0000000000000008L, 32));
    assertEquals(0x0000000000000003L, HHCodeHelper.southHHCode(0x0000000000000009L, 32));
    assertEquals(0x0000000000000006L, HHCodeHelper.southHHCode(0x000000000000000cL, 32));
    assertEquals(0x0000000000000007L, HHCodeHelper.southHHCode(0x000000000000000dL, 32));
    assertEquals(0x0000000000000008L, HHCodeHelper.southHHCode(0x000000000000000aL, 32));
    assertEquals(0x0000000000000009L, HHCodeHelper.southHHCode(0x000000000000000bL, 32));
    assertEquals(0x000000000000000cL, HHCodeHelper.southHHCode(0x000000000000000eL, 32));
    assertEquals(0x000000000000000dL, HHCodeHelper.southHHCode(0x000000000000000fL, 32));
    assertEquals(0x000000000000000aL, HHCodeHelper.southHHCode(0x0000000000000020L, 32));
    assertEquals(0x000000000000000bL, HHCodeHelper.southHHCode(0x0000000000000021L, 32));
    assertEquals(0x000000000000000eL, HHCodeHelper.southHHCode(0x0000000000000024L, 32));
    assertEquals(0x000000000000000fL, HHCodeHelper.southHHCode(0x0000000000000025L, 32));
    
    // Test wrap around
    
    assertEquals(0x4000000000000000L, HHCodeHelper.southHHCode(0x0000000000000000L, 32));
    assertEquals(0x5111111111111111L, HHCodeHelper.southHHCode(0x1111111111111111L, 32));
    assertEquals(0x0444444444444444L, HHCodeHelper.southHHCode(0x4444444444444444L, 32));
    assertEquals(0x1555555555555555L, HHCodeHelper.southHHCode(0x5555555555555555L, 32));    
  }
  
  @Test
  public void testRight() {
    assertEquals(0x1L, HHCodeHelper.eastHHCode(0L, 32));
    assertEquals(0x4L, HHCodeHelper.eastHHCode(1L, 32));
    assertEquals(0x5L, HHCodeHelper.eastHHCode(4L, 32));
    assertEquals(0x3L, HHCodeHelper.eastHHCode(2L, 32));
    assertEquals(0x6L, HHCodeHelper.eastHHCode(3L, 32));
    assertEquals(0x7L, HHCodeHelper.eastHHCode(6L, 32));
    assertEquals(0x9L, HHCodeHelper.eastHHCode(8L, 32));
    assertEquals(0xcL, HHCodeHelper.eastHHCode(9L, 32));
    assertEquals(0xdL, HHCodeHelper.eastHHCode(0xcL, 32));
    
    assertEquals(0x10L, HHCodeHelper.eastHHCode(5L, 32));
    assertEquals(0x12L, HHCodeHelper.eastHHCode(7L, 32));
    assertEquals(0x18L, HHCodeHelper.eastHHCode(0xdL, 32));
    assertEquals(0x1aL, HHCodeHelper.eastHHCode(0xfL, 32));

    // Test wrap around
    
    assertEquals(0x0000000000000000L, HHCodeHelper.eastHHCode(0x5555555555555555L, 32));
    assertEquals(0x2222222222222222L, HHCodeHelper.eastHHCode(0x7777777777777777L, 32));
    assertEquals(0x8888888888888888L, HHCodeHelper.eastHHCode(0xddddddddddddddddL, 32));
    assertEquals(0xaaaaaaaaaaaaaaaaL, HHCodeHelper.eastHHCode(0xffffffffffffffffL, 32));
  }

  @Test
  public void testLeft() {
    assertEquals(0x1L, HHCodeHelper.westHHCode(4L, 32));
    assertEquals(0x4L, HHCodeHelper.westHHCode(5L, 32));
    assertEquals(0x5L, HHCodeHelper.westHHCode(0x10L, 32));
    assertEquals(0x3L, HHCodeHelper.westHHCode(6L, 32));
    assertEquals(0x6L, HHCodeHelper.westHHCode(7L, 32));
    assertEquals(0x7L, HHCodeHelper.westHHCode(0x12L, 32));
    assertEquals(0x9L, HHCodeHelper.westHHCode(0xcL, 32));
    assertEquals(0xcL, HHCodeHelper.westHHCode(0xdL, 32));
    assertEquals(0xdL, HHCodeHelper.westHHCode(0x18L, 32));
    
    assertEquals(0x0L, HHCodeHelper.westHHCode(1L, 32));
    assertEquals(0x2L, HHCodeHelper.westHHCode(3L, 32));
    assertEquals(0x8L, HHCodeHelper.westHHCode(9L, 32));
    assertEquals(0xaL, HHCodeHelper.westHHCode(0xbL, 32));
    
    // Test random values, assuming testRight passed
    
    for (int r = 32; r > 0; r--) {
      for (int i = 0; i < 100; i++) {
        long hhcode = Math.round(Math.random() * (1L << 64));
        
        assertEquals(hhcode, HHCodeHelper.westHHCode(HHCodeHelper.eastHHCode(hhcode, r), r));        
      }
    }
    
    //
    // Test wrap around
    //
    
    assertEquals(0x5555555555555555L, HHCodeHelper.westHHCode(0x0000000000000000L, 32));
    assertEquals(0x7777777777777777L, HHCodeHelper.westHHCode(0x2222222222222222L, 32));
    assertEquals(0xddddddddddddddddL, HHCodeHelper.westHHCode(0x8888888888888888L, 32));
    assertEquals(0xffffffffffffffffL, HHCodeHelper.westHHCode(0xaaaaaaaaaaaaaaaaL, 32));
  }
  
  @Test
  public void testToString() {
    assertEquals("0000000000000000", HHCodeHelper.toString(0L));
    assertEquals("123456789abcdef0", HHCodeHelper.toString(0x123456789abcdef0L));
    
    assertEquals("1", HHCodeHelper.toString(0x123456789abcdef0L, 2));
    assertEquals("12", HHCodeHelper.toString(0x123456789abcdef0L, 4));
    assertEquals("123", HHCodeHelper.toString(0x123456789abcdef0L, 6));
    assertEquals("1234", HHCodeHelper.toString(0x123456789abcdef0L, 8));
    assertEquals("12345", HHCodeHelper.toString(0x123456789abcdef0L, 10));
    assertEquals("123456", HHCodeHelper.toString(0x123456789abcdef0L, 12));
    assertEquals("1234567", HHCodeHelper.toString(0x123456789abcdef0L, 14));
    assertEquals("12345678", HHCodeHelper.toString(0x123456789abcdef0L, 16));
    assertEquals("123456789", HHCodeHelper.toString(0x123456789abcdef0L, 18));
    assertEquals("123456789a", HHCodeHelper.toString(0x123456789abcdef0L, 20));
    assertEquals("123456789ab", HHCodeHelper.toString(0x123456789abcdef0L, 22));
    assertEquals("123456789abc", HHCodeHelper.toString(0x123456789abcdef0L, 24));
    assertEquals("123456789abcd", HHCodeHelper.toString(0x123456789abcdef0L, 26));
    assertEquals("123456789abcde", HHCodeHelper.toString(0x123456789abcdef0L, 28));
    assertEquals("123456789abcdef", HHCodeHelper.toString(0x123456789abcdef0L, 30));
  }
  
  @Test
  public void testCoverRectangle() {
    Coverage coverage = HHCodeHelper.coverRectangle(-90,-180,90,180);
    coverage.optimize(0L);
    assertEquals("0 9 2 b 4 d 6 f 8 1 a 3 c 5 e 7", coverage.toString());
    coverage = HHCodeHelper.coverRectangle(-90,-180,90.0,-0.0000001);
    coverage.optimize(0L);
    assertEquals("8 0 9 1 a 2 b 3", coverage.toString());
    coverage = HHCodeHelper.coverRectangle(-90,-180,-0.0000001,-0.0000001);
    coverage.optimize(0L);
    assertEquals("0 1 2 3", coverage.toString());
    coverage = HHCodeHelper.coverRectangle(0, 0, 90, 180);
    coverage.optimize(0L);
    assertEquals("c d e f", coverage.toString());
    coverage = HHCodeHelper.coverRectangle(43, -5.5, 51.2, 6.1);
    coverage.optimize(0L);
    assertEquals("e08 b5d e09 e03 e02 b57 e00 b55 caa 9ff cab e01", coverage.toString());
  }
  
  /**
   * Test coverRectangle when we cross the international date line
   */
  @Test
  public void testCoverRectangleIDL() {
    
    Coverage coverage = HHCodeHelper.coverRectangle(0,0,90,190);
    coverage.optimize(0L);
    assertEquals("8 a c d e f", coverage.toString());
  
    coverage = HHCodeHelper.coverRectangle(0,-270,90,0);
    coverage.optimize(0L);
    // We would expect 8 9 a b f d BUT 0 degrees of lat is shifted to 90 degrees of lat which
    // lies in F/D cells (the first point of those cells)
    assertEquals("8 9 a b c d e f", coverage.toString());

    coverage = HHCodeHelper.coverRectangle(0,-270,90,-0.0000000001);
    coverage.optimize(0L);
    assertEquals("8 9 a b d f", coverage.toString());

    // Now attempt to get a coverage for a rectangle which covers more than 360 degrees of lon.
    
    coverage = HHCodeHelper.coverRectangle(-90,-180,90,200);
    Assert.assertEquals("0 9 2 b 4 d 6 f 8 1 a 3 c 5 e 7", coverage.toString());
  }

  @Test
  public void testCoverLine() {
    
    long from = HHCodeHelper.getHHCodeValue(0, -90);
    long to = HHCodeHelper.getHHCodeValue(0, 90);
    
    Coverage coverage = new Coverage();
    
    HHCodeHelper.coverLine(from, to, coverage, 0);
    
    Assert.assertEquals("900 c45 940 c05 944 c01 904 c41 c44 901 c04 941 c00 945 c40 905 911 c54 951 c14 955 c10 915 c50 c55 910 c15 950 c11 954 c51 914", coverage.toString());
    
    from = HHCodeHelper.getHHCodeValue(-45, 0);
    to = HHCodeHelper.getHHCodeValue(45, 0);

    coverage.clear();
    
    HHCodeHelper.coverLine(from, to, coverage, 0);
    
    Assert.assertEquals("c08 682 c2a 6a0 c88 602 caa 620 c80 60a ca2 628 c00 68a c22 6a8 6a2 c28 680 c0a 622 ca8 600 c8a 62a ca0 608 c82 6aa c20 688 c02", coverage.toString());

    from = HHCodeHelper.getHHCodeValue(-45, -90);
    to = HHCodeHelper.getHHCodeValue(45, 90);

    coverage.clear();
    
    HHCodeHelper.coverLine(from, to, coverage, 0);
    
    Assert.assertEquals("ccc 303 33f cf0 3ff c30 c0c 3c3 333 cfc cc0 30f c00 3cf 3f3 c3c cc3 30c 330 cff 3f0 c3f c03 3cc 33c cf3 ccf 300 c0f 3c0 3fc c33", coverage.toString());
    
    from = HHCodeHelper.getHHCodeValue(-45, 90);
    to = HHCodeHelper.getHHCodeValue(45, -90);

    coverage.clear();
    
    HHCodeHelper.coverLine(from, to, coverage, 0);

    Assert.assertEquals("69c 9a6 99a 96b 695 6a9 957 9a7 659 665 970 99b 96a 956 66c 666 6c0 95c b00 65a 955 6ab 697 969 999 667 65b 9a5 6b0 6aa 9ac 696 669 997 9ab 655 95a 65c 966 9c0 996 6ac 9aa 9b0 c00 6a5 95b 967 699 657 9a9 995 66b 69a 6a6 99c 96c 656 66a 69b 670 965 959 6a7", coverage.toString());

  }
  
  @Test
  public void testCoverPolyline() {

    List<Long> vertices = new ArrayList<Long>() {{
      add(HHCodeHelper.getHHCodeValue(-45.0, -90.0));
      //add(HHCodeHelper.getHHCodeValue(-90.0, 180.0));
      //add(HHCodeHelper.getHHCodeValue(90.0, 180.0));
      add(HHCodeHelper.getHHCodeValue(-50.0, 90.0));      
    }};

    long nano = System.nanoTime();
    Coverage coverage = HHCodeHelper.coverPolyline(vertices, 0,false);
    Assert.assertEquals("4fc 1ea 4bc 1aa 4b8 1ae 4f8 4ab 1ee 1eb 4fd 4ae 1ab 4bd 1af 4b9 1ef 4f9 4aa 4ed 1fb 4ad 1bb 1bf 4e9 1ff 1fa 4ec 1ba 300 4ac 1be 1fe 4e8", coverage.toString());
  }
  
  @Test
  public void testCoverPolyline_PerSegmentResolution() {
    List<Long> lats = new ArrayList<Long>();
    List<Long> lons = new ArrayList<Long>();
    
    lats.add(HHCodeHelper.toLongLat(-1.0));
    lons.add(HHCodeHelper.toLongLat(-90.0));
    lats.add(HHCodeHelper.toLongLat(0.0));
    lons.add(HHCodeHelper.toLongLat(-90.0));
    lats.add(HHCodeHelper.toLongLat(0));
    lons.add(HHCodeHelper.toLongLat(0.0));
    
    Coverage coverage = HHCodeHelper.coverPolyline(lats, lons, 0, true, false);
    
    Assert.assertEquals("900 801 940 841 944 845 904 805 800 901 840 941 844 945 804 905 911 810 951 850 955 854 915 814 811 910 851 950 855 954 815 914 2aaaa8 2aa888 2aaa88 2aa8a8 2aaaaa 2aa88a 2aaa8a 2aa8aa 2aaa28 2aaa08 2aaa2a 2aaa0a 2aaaa0 2aaa80 2aa8a0 2aaaa2 2aa882 2aaa82 2aa8a2 2aaa20 2aaa00 2aaa22 2aaa02", coverage.toString());
  }
  
  @Test
  public void testCoverPolygon() {
    
    List<Long> vertices = new ArrayList<Long>() {{
      add(HHCodeHelper.getHHCodeValue(-90.0, -179.999999));
      add(HHCodeHelper.getHHCodeValue(-90.0, 179.999999));
      add(HHCodeHelper.getHHCodeValue(89.99999, 179.999999));
      add(HHCodeHelper.getHHCodeValue(89.99999, -179.999999));      
    }};
    
    long nano = System.nanoTime();
    Coverage coverage = HHCodeHelper.coverPolygon(vertices, 8);
    coverage.optimize(0x0000000000000000L);
    assertEquals("0 9 2 b 4 d 6 f 8 1 a 3 c 5 e 7", coverage.toString());
      
    vertices = new ArrayList<Long>() {{
      add(HHCodeHelper.getHHCodeValue(51.344338660599234,2.548828125));
      add(HHCodeHelper.getHHCodeValue(48.574789910928864,-5.537109375));
      add(HHCodeHelper.getHHCodeValue(43.45291889355465,-1.93359375));
      add(HHCodeHelper.getHHCodeValue(42.09822241118974,3.515625));
      add(HHCodeHelper.getHHCodeValue(43.89789239125797,8.876953125));
      add(HHCodeHelper.getHHCodeValue(49.0954521625348,8.701171875));
    }};

    nano = System.nanoTime();
    coverage = HHCodeHelper.coverPolygon(vertices, 12);
    coverage.optimize(0x0000000000000000L);
    int ncells = 0;
    for (int resolution = 2; resolution <= 32; resolution += 2) {
      ncells += coverage.getCells(resolution).size();
    }
    
    Assert.assertEquals(1395, ncells);

    Assert.assertEquals(1, coverage.getCells(6).size());
    Assert.assertEquals(57, coverage.getCells(8).size());
    Assert.assertEquals(273, coverage.getCells(10).size());
    Assert.assertEquals(1064, coverage.getCells(12).size());
    
  }
    
  @Test
  public void testCoverPolygonIDL() {
    List<Long> verticesLat = new ArrayList<Long>();
    List<Long> verticesLon = new ArrayList<Long>();
    
    verticesLat.add(HHCodeHelper.toLongLat(-90.0));
    verticesLon.add(HHCodeHelper.toLongLon(0.0));

    verticesLat.add(HHCodeHelper.toLongLat(90.0));
    verticesLon.add(HHCodeHelper.toLongLon(270));

    verticesLat.add(HHCodeHelper.toLongLat(90.0));
    verticesLon.add(HHCodeHelper.toLongLon(0.0));
    
    Coverage coverage = HHCodeHelper.coverPolygon(verticesLat, verticesLon, 0);
    
    assertEquals("8 a b c 4 d e 6 f 7", coverage.toString());
  }
  
  @Test
  public void testToIndexableString() {
    assertEquals("0123456789abcdef 0 01 012 0123 01234 012345 0123456 01234567 012345678 0123456789 0123456789a 0123456789ab 0123456789abc 0123456789abcd 0123456789abcde", HHCodeHelper.toIndexableString(0x0123456789abcdefL));
    assertEquals("0123456789abcdef 0 01", HHCodeHelper.toIndexableString(0x0123456789abcdefL, 2, 4));
    assertEquals("0123456789abcdef 012 0123 01234", HHCodeHelper.toIndexableString(0x0123456789abcdefL, 6, 10));
  }
  
  @Test
  public void testFromString() {
    assertEquals(0xffffffffffffffffL, HHCodeHelper.fromString("ffffffffffffffff"));
    assertEquals(0xfffffffffffffff0L, HHCodeHelper.fromString("fffffffffffffff"));
    assertEquals(0xffffffffffffff00L, HHCodeHelper.fromString("ffffffffffffff"));
    assertEquals(0xfffffffffffff000L, HHCodeHelper.fromString("fffffffffffff"));
    assertEquals(0xffffffffffff0000L, HHCodeHelper.fromString("ffffffffffff"));
    assertEquals(0xfffffffffff00000L, HHCodeHelper.fromString("fffffffffff"));    
    assertEquals(0xffffffffff000000L, HHCodeHelper.fromString("ffffffffff"));    
    assertEquals(0xfffffffff0000000L, HHCodeHelper.fromString("fffffffff"));    
    assertEquals(0xffffffff00000000L, HHCodeHelper.fromString("ffffffff"));    
    assertEquals(0xfffffff000000000L, HHCodeHelper.fromString("fffffff"));    
    assertEquals(0xffffff0000000000L, HHCodeHelper.fromString("ffffff"));    
    assertEquals(0xfffff00000000000L, HHCodeHelper.fromString("fffff"));    
    assertEquals(0xffff000000000000L, HHCodeHelper.fromString("ffff"));    
    assertEquals(0xfff0000000000000L, HHCodeHelper.fromString("fff"));    
    assertEquals(0xff00000000000000L, HHCodeHelper.fromString("ff"));    
    assertEquals(0xf000000000000000L, HHCodeHelper.fromString("f"));    
  }
  
  @Test
  public void testToGeoCells() {
    long hhcode = 0x1234567890abcdefL;
    
    long[] geocells = HHCodeHelper.toGeoCells(hhcode);
    
    Assert.assertEquals(15, geocells.length);
    Assert.assertEquals(0x1100000000000000L, geocells[0]);
    Assert.assertEquals(0x2120000000000000L, geocells[1]);
    Assert.assertEquals(0x3123000000000000L, geocells[2]);
    Assert.assertEquals(0x4123400000000000L, geocells[3]);
    Assert.assertEquals(0x5123450000000000L, geocells[4]);
    Assert.assertEquals(0x6123456000000000L, geocells[5]);
    Assert.assertEquals(0x7123456700000000L, geocells[6]);
    Assert.assertEquals(0x8123456780000000L, geocells[7]);
    Assert.assertEquals(0x9123456789000000L, geocells[8]);
    Assert.assertEquals(0xa123456789000000L, geocells[9]);
    Assert.assertEquals(0xb1234567890a0000L, geocells[10]);    
    Assert.assertEquals(0xc1234567890ab000L, geocells[11]);
    Assert.assertEquals(0xd1234567890abc00L, geocells[12]);
    Assert.assertEquals(0xe1234567890abcd0L, geocells[13]);
    Assert.assertEquals(0xf1234567890abcdeL, geocells[14]);

    //
    // Now check geocells with a specified finest resolution
    //
    
    geocells = HHCodeHelper.toGeoCells(hhcode, 16);
    Assert.assertEquals(8, geocells.length);
    Assert.assertEquals(0x1100000000000000L, geocells[0]);
    Assert.assertEquals(0x2120000000000000L, geocells[1]);
    Assert.assertEquals(0x3123000000000000L, geocells[2]);
    Assert.assertEquals(0x4123400000000000L, geocells[3]);
    Assert.assertEquals(0x5123450000000000L, geocells[4]);
    Assert.assertEquals(0x6123456000000000L, geocells[5]);
    Assert.assertEquals(0x7123456700000000L, geocells[6]);
    Assert.assertEquals(0x8123456780000000L, geocells[7]);        
  }
  
  @Test
  public void testToGeoCell() {
    Assert.assertEquals(0x1100000000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 2));
    Assert.assertEquals(0x2120000000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 4));
    Assert.assertEquals(0x3123000000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 6));
    Assert.assertEquals(0x4123400000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 8));
    Assert.assertEquals(0x5123450000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 10));
    Assert.assertEquals(0x6123456000000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 12));
    Assert.assertEquals(0x7123456700000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 14));
    Assert.assertEquals(0x8123456780000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 16));
    Assert.assertEquals(0x9123456789000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 18));
    Assert.assertEquals(0xa123456789000000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 20));
    Assert.assertEquals(0xb1234567890a0000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 22));    
    Assert.assertEquals(0xc1234567890ab000L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 24));
    Assert.assertEquals(0xd1234567890abc00L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 26));
    Assert.assertEquals(0xe1234567890abcd0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 28));
    Assert.assertEquals(0xf1234567890abcdeL, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 30));    
  }
  
  @Test
  public void testToRegexp() {
    long hhcode = 0xf234567890abcdefL;
    
    long[] geocells = HHCodeHelper.toGeoCells(hhcode);
    
    String regexp = HHCodeHelper.geocellsToRegexp(geocells);
    
    System.out.println(regexp);
  }
  @Test
  public void testToGeoCell_InvalidResolution() {
    Assert.assertEquals(0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 0));
    Assert.assertEquals(0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 32));
    Assert.assertEquals(0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 31));
    Assert.assertEquals(0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, 5));
    Assert.assertEquals(0L, HHCodeHelper.toGeoCell(0x1234567890abcdefL, -2));
    
    for (int i = 2; i < 32; i+=2) {
      Assert.assertTrue(0L != HHCodeHelper.toGeoCell(0x1234567890abcdefL, i));
    }
  }

  @Test
  public void testGcIntermediate() {
    double flat = -45.0;
    double flon = 50.0;
    
    double tlat = -45.0;
    double tlon = 229.0;
    
    for (double f = 0.0; f < 1.0; f += 0.10) {
    long[] inter = HHCodeHelper.gcIntermediate(HHCodeHelper.toLongLat(flat), HHCodeHelper.toLongLon(flon), HHCodeHelper.toLongLat(tlat), HHCodeHelper.toLongLon(tlon), f);
    
    double lat = HHCodeHelper.toLat(inter[0]);
    double lon = HHCodeHelper.toLon(inter[1]);
    
    System.out.println("F=" + f + "   lat=" + lat + "  lon=" + lon);
    
    //20585569,4463000320 >>> 27848991,287979213 >>> 35610213,4652052633

    inter = HHCodeHelper.gcIntermediate(20585569, 4463000320L, 35610213, 4652052633L, 0.5);
    lat = HHCodeHelper.toLat(inter[0]);
    lon = HHCodeHelper.toLon(inter[1]);
    
    System.out.println("F=" + f + "   lat=" + lat + "  lon=" + lon);

    }
  }

  @Test
  public void testOrthodromize() {
    double flat = -45.0;
    double flon = 50.0;
    
    double tlat = -45.0;
    double tlon = 229.0;
    List<Long> orthodromy = HHCodeHelper.orthodromize(HHCodeHelper.toLongLat(flat), HHCodeHelper.toLongLon(flon), HHCodeHelper.toLongLat(tlat), HHCodeHelper.toLongLon(tlon), 1.01);

    for (int i = 0; i < orthodromy.size(); i += 2) {
      System.out.print(HHCodeHelper.toLat(orthodromy.get(i)));
      System.out.print(",");
      System.out.print(HHCodeHelper.toLon(orthodromy.get(i+1)));
      System.out.print(" >>> ");
    }
    /*
    */
    System.out.println();
  }

  @Test
  public void testCoverWithGeoCells() throws Exception {
    // First create a cirle
    
    Coverage clip = GeoParser.parseCircle("48.0:-4.5:500", -2);
    clip.optimize(0L);
    

    Writer writer = new FileWriter("/var/tmp/tst1-clip.kml");    
    CoverageHelper.toKML(clip, writer, true);
    writer.close();
    
    long[] geocells = clip.toGeoCells(30);
    System.out.println(geocells.length);
    
    Arrays.sort(geocells);
    
    // Then cover another circle clipped to the precedent one
    
    Coverage coverage = HHCodeHelper.coverRectangle(47.99, -4.51, 48.01, -4.49, 20, geocells, true);
    
    writer = new FileWriter("/var/tmp/tst1.kml");
    
    coverage.optimize(0L);
    CoverageHelper.toKML(coverage, writer, true);
    
    writer.close();
  }
  
  @Test
  public void testGetSubGeoCells() {
    long geocell = 0x1000000000000000L;
    
    long[] subcells = HHCodeHelper.getSubGeoCells(geocell);
    ArrayList<Long> cells = new ArrayList<Long>();
    for (long subcell: subcells) {
      cells.add(subcell);
    }
    
    Assert.assertEquals(16, cells.size());
    Assert.assertTrue(cells.contains(0x2000000000000000L));
    Assert.assertTrue(cells.contains(0x2010000000000000L));
    Assert.assertTrue(cells.contains(0x2020000000000000L));
    Assert.assertTrue(cells.contains(0x2030000000000000L));
    Assert.assertTrue(cells.contains(0x2040000000000000L));
    Assert.assertTrue(cells.contains(0x2050000000000000L));
    Assert.assertTrue(cells.contains(0x2060000000000000L));
    Assert.assertTrue(cells.contains(0x2070000000000000L));
    Assert.assertTrue(cells.contains(0x2080000000000000L));
    Assert.assertTrue(cells.contains(0x2090000000000000L));
    Assert.assertTrue(cells.contains(0x20a0000000000000L));
    Assert.assertTrue(cells.contains(0x20b0000000000000L));
    Assert.assertTrue(cells.contains(0x20c0000000000000L));
    Assert.assertTrue(cells.contains(0x20d0000000000000L));
    Assert.assertTrue(cells.contains(0x20e0000000000000L));
    Assert.assertTrue(cells.contains(0x20f0000000000000L));    
    
    //
    // Now at res 30
    //
    
    geocell = 0xf000000000000000L;

    subcells = HHCodeHelper.getSubGeoCells(geocell);
    cells = new ArrayList<Long>();
    for (long subcell: subcells) {
      cells.add(subcell);
    }
    
    Assert.assertEquals(16, cells.size());
    for (long i = 0; i < 16; i++) {
      Assert.assertTrue(cells.contains(i));
    }
  }

  @Test
  public void testOrthodromicDistance() {
    long from = HHCodeHelper.getHHCodeValue(48.0, -4.55);
    long to = HHCodeHelper.getHHCodeValue(48.0, -4.50);
    
    Assert.assertEquals(3717.688116D, HHCodeHelper.orthodromicDistance(from, to), 0.000001D);
    
    to = HHCodeHelper.getHHCodeValue(49.0, -4.55);
    Assert.assertEquals(111119.998033D, HHCodeHelper.orthodromicDistance(from, to), 0.000001D);    
    
    to = HHCodeHelper.getHHCodeValue(-48.0, 4.55);
    Assert.assertEquals(10703606.805314D, HHCodeHelper.orthodromicDistance(from, to), 0.000001D);    
  }
  
  @Test
  public void testLoxodromicDistance() {
    long from = HHCodeHelper.getHHCodeValue(48.0, -4.55);
    long to = HHCodeHelper.getHHCodeValue(48.0, -4.50);
    
    Assert.assertEquals(3717.688181D, HHCodeHelper.loxodromicDistance(from, to), 0.000001D);
    
    to = HHCodeHelper.getHHCodeValue(49.0, -4.55);
    Assert.assertEquals(111119.998033D, HHCodeHelper.loxodromicDistance(from, to), 0.000001D);
    
    to = HHCodeHelper.getHHCodeValue(-48.0, 4.55);
    Assert.assertEquals(10715339.110828D, HHCodeHelper.loxodromicDistance(from, to), 0.000001D);    
  }
  
  @Test
  public void testGetCenterLatLon() {
    
    long hhcode = HHCodeHelper.getHHCodeValue(48.0, -4.5D);
    
    double[] latlon = HHCodeHelper.getCenterLatLon(hhcode, 26);
    System.out.println(Arrays.toString(latlon));    
    latlon = HHCodeHelper.getLatLon(hhcode, 26);
    System.out.println(Arrays.toString(latlon));    
  }
  
  @Test
  public void testParentGeoCell() {
    long cell = 0xF123456789ABCDEFL;
    
    long[] parents = new long[] { 0xE123456789ABCDE0L, 0xD123456789ABCD00L, 0xC123456789ABC000L, 0xB123456789AB0000L,
        0xA123456789A00000L, 0x9123456789000000L, 0x8123456780000000L, 0x7123456700000000L,
        0x6123456000000000L, 0x5123450000000000L, 0x4123400000000000L, 0x3123000000000000L,
        0x2120000000000000L, 0x1100000000000000L,
    };
    
    for (int i = 0; i < parents.length; i++) {
      cell = HHCodeHelper.parentGeoCell(cell);
      Assert.assertEquals(parents[i], cell);
    }
  }
  
  public static void main(String[] args) {
    HHCodeHelperTestCase tc = new HHCodeHelperTestCase();
    tc.testCoverPolygonIDL();
  }
  
}

