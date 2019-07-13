package com.healthsystem.util;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class AzureBlob {
	private AzureBlob() {

	}

	private static CloudBlobClient createClient() {
		CloudBlobClient client = null;
		try {
			String connectionString = String.format(
					"");
			CloudStorageAccount account = CloudStorageAccount.parse(connectionString);
			client = account.createCloudBlobClient();
		} catch (Exception e) {

		}
		return client;
	}

	public static void upload(BufferedImage buffered, String name, String container) {

		try {

			CloudBlobClient client = createClient();
			CloudBlobContainer ctner = client.getContainerReference(container);
			CloudBlockBlob blob = ctner.getBlockBlobReference(name);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(buffered, "jpg", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			blob.upload(is, is.available());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void upload(InputStream is, String name, String container) {

		try {

			CloudBlobClient client = createClient();
			CloudBlobContainer ctner = client.getContainerReference(container);
			CloudBlockBlob blob = ctner.getBlockBlobReference(name);

			blob.upload(is, is.available());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void upload(File file, String name, String container) {
		InputStream is = null;

		try {
			is = new FileInputStream(file);

			CloudBlobClient client = createClient();
			CloudBlobContainer ctner = client.getContainerReference(container);
			CloudBlockBlob blob = ctner.getBlockBlobReference(name);
			blob.upload(is, is.available());

			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		} catch (StorageException ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public static BufferedImage download(String name, String container) {

		try {

			CloudBlobClient client = createClient();
			CloudBlobContainer ctner = client.getContainerReference(container);
			CloudBlockBlob blob = ctner.getBlockBlobReference(name);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			blob.download(os);
			byte[] data = os.toByteArray();
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			BufferedImage image = ImageIO.read(input);

			return image;
		} catch (Exception e) {
			return null;
		}

	}

	public static byte[] downloadInputStream(String name, String container) {

		try {

			CloudBlobClient client = createClient();
			CloudBlobContainer ctner = client.getContainerReference(container);
			CloudBlockBlob blob = ctner.getBlockBlobReference(name);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			blob.download(os);
			byte[] data = os.toByteArray();

			return data;
		} catch (Exception e) {
			return null;
		}

	}

	public static void delete(String name, String containerName) {

		try {

			CloudBlobClient client = createClient();
			CloudBlobContainer container = client.getContainerReference(containerName);
			CloudBlockBlob blob = container.getBlockBlobReference(name);
			blob.delete();
		} catch (Exception e) {

		}

	}
}
