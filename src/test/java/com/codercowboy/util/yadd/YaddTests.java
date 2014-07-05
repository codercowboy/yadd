package com.codercowboy.util.yadd;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class YaddTests {	
	@Rule public TestName testName = new TestName();
	
	@Before
    public void before() {
        System.out.println("-------- Now Executing Test: " + getClass().getSimpleName() + ":" + testName.getMethodName());
    }
		
	protected static String littleFilePath;
	protected static String bigFilePath;
	protected static String baseFilePath;
	protected static final String testFilePath = "testFiles";
	protected static final boolean runPerformanceTests = false;

	@BeforeClass
	public static void beforeClass() throws Exception {
		File directory = File.createTempFile(testFilePath, "");		
		directory.delete();
		directory.mkdir();
		baseFilePath = directory.getAbsolutePath();
		System.out.println("Base File Path: " + baseFilePath);
		littleFilePath = createTestFile("littlefile", "A small amount of data.");		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1024 * 256; i++) {
			sb.append("Some Random Data");
		}		
		bigFilePath = createTestFile("bigFile", sb.toString());		
		
		
	}
	
	public static String createTestFile(String name, String data) throws Exception {
		File file = File.createTempFile( name, ".txt", new File(baseFilePath));
		FileUtils.writeStringToFile(file, data, "UTF-8");
		file.deleteOnExit();
		System.out.println("Absolute path for test file: " + file.getAbsolutePath());
		return file.getAbsolutePath();
	}
	
	@Test 
	public void testIndexFolder() throws Exception {
		//TODO: test null inputs, file instead of folder input, etc.
		List<FileInfo> files = FileIndexer.indexFolder(new File(baseFilePath));
		for (FileInfo f: files) {
			System.out.println("file: " + f);
		}
		//TODO: ensure indexer always spits out alphabetically
		assertEquals(FilenameUtils.getBaseName(bigFilePath) + ".txt", files.get(0).getRelativePath());
		assertEquals("91221dbc4e40c403edd758d0c76b2bf1", files.get(0).getMd5Checksum());
		assertEquals(4194304, files.get(0).getFileSize());
		assertEquals(FilenameUtils.getBaseName(littleFilePath) + ".txt", files.get(1).getRelativePath());
		assertEquals("f9917299aea378770c808801e21ad381", files.get(1).getMd5Checksum());
		assertEquals(23, files.get(1).getFileSize());
	}
	
	@Test
	public void testIndexerMD5Checksums() throws Exception {
		assertEquals("f9917299aea378770c808801e21ad381", FileIndexer.getMD5ChecksumForFile(new File(littleFilePath)));
		assertEquals("91221dbc4e40c403edd758d0c76b2bf1", FileIndexer.getMD5ChecksumForFile(new File(bigFilePath)));
	}
	
	@Test
	public void testIndexerPerformance() throws Exception {
		if (!runPerformanceTests) {
			System.out.println("Skipping indexer performance test, performance tests turned off.");
			return;
		}
		int iterationCount = 1000;
		System.out.println("Now running " + iterationCount + " iterations of indexer test.");
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < iterationCount; i++) {
			assertEquals("f9917299aea378770c808801e21ad381", FileIndexer.getMD5ChecksumForFile(new File(littleFilePath)));
			assertEquals("91221dbc4e40c403edd758d0c76b2bf1", FileIndexer.getMD5ChecksumForFile(new File(bigFilePath)));
		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Finished indexer performance test. Total time elapsed: " + DurationFormatUtils.formatDurationHMS(duration) + ", (" + (duration / iterationCount) + "ms per iteration)");
	}
	
	
}
