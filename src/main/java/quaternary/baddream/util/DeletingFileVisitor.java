package quaternary.baddream.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DeletingFileVisitor extends SimpleFileVisitor<Path> {
	List<IOException> exceptions = new ArrayList<>();
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		exceptions.add(exc);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		Files.delete(dir);
		return FileVisitResult.CONTINUE;
	}
	
	public void throwAllErrors() {
		if(!exceptions.isEmpty()) {
			RuntimeException yeah = new RuntimeException("Problems in DeletingFileVisitor:");
			exceptions.forEach(yeah::addSuppressed);
			throw yeah;
		}
	}
}
