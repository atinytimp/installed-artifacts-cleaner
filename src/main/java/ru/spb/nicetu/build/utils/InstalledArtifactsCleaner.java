package ru.spb.nicetu.build.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class InstalledArtifactsCleaner
{
    public static void main( String[] args ) throws IOException, XmlPullParserException {

        File repositoryRoot = null;
        String metadataFileName = "maven-metadata-local.xml";

        if(args.length == 0 || args[0] == null || "".equals(args[0])) {
            repositoryRoot = new File("C:\\Users\\petrov\\.m2\\repository");
        }

        Collection<File> metadataFiles = FileUtils.listFiles(repositoryRoot, FileFilterUtils.nameFileFilter(metadataFileName), FileFilterUtils.directoryFileFilter());
        MetadataXpp3Reader metadataReader = new MetadataXpp3Reader();

        for(File metadataFile : metadataFiles) {

            FileReader fileReader = new FileReader(metadataFile);
            Metadata metadata = metadataReader.read(fileReader);
            Versioning versioning = metadata.getVersioning();
            List<String> versions = versioning.getVersions();

            for (String version : versions) {

                String directory = metadataFile.getParent() + "\\" + version;
                System.out.println("REMOVING:" + directory);
                FileUtils.deleteDirectory(new File(directory));
            }

            fileReader.close();
            FileUtils.deleteQuietly(metadataFile);
        }
    }
}
