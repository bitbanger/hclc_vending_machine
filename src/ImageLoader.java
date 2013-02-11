import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

/**
 * Contains static methods used to load images needed for the GUI.
 * @author Matthew Koontz
 **/
public class ImageLoader
{
	/**
	 * Root path for all images.
	 **/
	private static final String IMAGE_ROOT = "/";

	/**
	 * Path to item purchased notification image.
	 **/
	private static final String PURCHASE_ITEM = "item_purchase.png";

	/**
	 * Private constructor because this class should not be instantiated.
	 **/
	private ImageLoader() {}

	/**
	 * Loads an image using the given path.
	 * @param path The path to the image (not including the root path).
	 * @return A BufferedImage instance for the given image.
	 **/
	private static BufferedImage loadImageByName(String path)
	{
		try
		{
			return ImageIO.read(ImageLoader.class.getResource(IMAGE_ROOT + path));
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Loads the image for the item purchased notification.
	 **/
	public static BufferedImage loadItemPurchaseImage()
	{
		return loadImageByName(PURCHASE_ITEM);
	}
}
