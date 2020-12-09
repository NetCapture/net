package test.junit;

import java.io.File;
import java.net.URLEncoder;

import org.junit.jupiter.api.Test;

import nicelee.github.upload.FileUploader;

class FileUploaderTest {

	// @Test
	void test() {
		String url = "https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/note/test.jpg";
		File file = new File(
				"D:\\Workspace\\GitWorkspace\\0_GitHub\\nICEnnnnnnnLee.github.io\\sources\\pics\\bg-catoon.jpg");
		boolean result = FileUploader.create(url, file, ":token");
		System.out.println(result);
	}

	//@Test
	void testSHA() {
		/// repos/:owner/:repo/contents/:path
		String url = "https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/note/bg-catoon.jpg";
		String result = FileUploader.getSHA(url);
		System.out.println(result);
	}

	@Test
	void testUPDATE() {
		String url = "https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/note/test.jpg";
		File file = new File(
				"D:\\Workspace\\GitWorkspace\\0_GitHub\\nICEnnnnnnnLee.github.io\\sources\\pics\\bg-catoon.jpg");
		boolean result = FileUploader.update(url, file, ":token");
		System.out.println(result);
	}

}
