<?
	set_time_limit(0);
	
	function arr($a)
	{
		echo "<pre>";
		print_r($a);
		echo "</pre>";
	};
	
	function unichr($u)
	{
    return mb_convert_encoding('&#'.$u.';', 'UTF-8', 'HTML-ENTITIES');
	}
	
	$IMG_SIZE = 512;
	$FONT_NAME = "Roboto-Thin";
	$FONT_SIZE = 18;
	$DEBUGGING = false;

	$FIRST_CHAR = 32;
	$LAST_CHAR = 1103;
	
	include("corrections.php");
	
	$im = @imagecreatetruecolor($IMG_SIZE, $IMG_SIZE);
	imagesavealpha($im, true);
	$color_alpha = imagecolorallocatealpha($im, 255, 255, 255, 127);
	if ($DEBUGGING)
		$color = imagecolorallocatealpha($im, 0, 0, 0, 0);
	else
		$color = imagecolorallocatealpha($im, 255, 255, 255, 0);
		
	$color0 = imagecolorallocatealpha($im, 200, 255, 200, 0);	
	$color1 = imagecolorallocatealpha($im, 255, 200, 200, 0);
	$color2 = imagecolorallocatealpha($im, 200, 200, 255, 0);
	
	imagefill($im, 0, 0, $color_alpha);

	$font = $FONT_NAME.".ttf";
	
	$index = $FONT_SIZE;
	if (!isset($CORRECTIONS[$FONT_NAME][$FONT_SIZE]))
		$index = 0;
		
	$CORR_BOX_X_SIZE = @$CORRECTIONS[$FONT_NAME][$index]["box-x-size"] + 0;
	$CORR_LINE_HEIGHT = @$CORRECTIONS[$FONT_NAME][$index]["line-height"] + 0;
	$CORR_Y_POS = @$CORRECTIONS[$FONT_NAME][$index]["y-pos"] + 0;
	$CORR_X_POS = @$CORRECTIONS[$FONT_NAME][$index]["x-pos"] + 0;
	$CORR_BOX_Y_SIZE = @$CORRECTIONS[$FONT_NAME][$index]["box-y-size"] + 0;
	
	$LAST_CHAR_DEF = @$CORRECTIONS[$FONT_NAME][$index]["last-char"] + 0;
	if ($LAST_CHAR_DEF == 0)
		$LAST_CHAR_DEF = $LAST_CHAR;
	$FIRST_CHAR_DEF = $FIRST_CHAR;

	
	$dim[0] = $dim[1] = 1e20;
	$dim[2] = $dim[3] = -1e20;
	
	for ($i = $FIRST_CHAR_DEF; $i <= $LAST_CHAR_DEF; $i++)
	{
		$text = unichr($i);
		$box = imagettfbbox($FONT_SIZE, 0, $font, $text);
		$dim[0] = min($box[0], $box[4], $dim[0]); 
    $dim[1] = min($box[1], $box[5], $dim[1]); 
    $dim[2] = max($box[0], $box[4], $dim[2]); 
    $dim[3] = max($box[1], $box[5], $dim[3]); 
	};
		
	$line_height = $dim[3] - $dim[1] + $CORR_LINE_HEIGHT;

	$class_name = "GLFont_".(str_replace('-', '', $FONT_NAME))."$FONT_SIZE";
	$file_name = strtolower("".(str_replace('-', '', $FONT_NAME))."$FONT_SIZE");
	
	$indices_arr = array();
	$sizes_arr = array();
	
	$class = mb_convert_encoding("", "UTF-8");
	$indices = mb_convert_encoding("", "UTF-8");
	$sizes = mb_convert_encoding("", "UTF-8");
	
	$class .= "package com.games;

import com.games.GLFont;
	
/**
 * Created by Ptysio on ".(date("Y-m-d H:i")).".
 */
public final class $class_name extends GLFont
{";
	
	$class .= "
	//minimal character code available
	public static final int FONT_MIN_INDEX = $FIRST_CHAR_DEF;
	
	//maximal character code available
	public static final int FONT_MAX_INDEX = $LAST_CHAR_DEF;
	
	//font name used to generate the class
	public static final String FONT_NAME = \"$FONT_NAME.ttf\";
	
	//file name with the texture
	public static final String FONT_TEXTURE_NAME = \"$file_name\";
	
	//font size used to generate the class (in pixels)
	public static final int FONT_SIZE = $FONT_SIZE;
	
	//texture dimensions
	public static final int FONT_TEXTURE_WIDTH = $IMG_SIZE;
	public static final int FONT_TEXTURE_HEIGHT = $IMG_SIZE;
	
	//texture coordinates
	public static final float[] FONT_TEXTURE_COORDS = new float[]
	{";

	$x = 0;
	$y = 0;
	$img_dim = $IMG_SIZE - 1;
	if ($DEBUGGING)
		imagerectangle($im, 0, 0, $img_dim, $img_dim, $color0);

	$char_index = 0;
	for ($i = $FIRST_CHAR_DEF; $i <= $LAST_CHAR_DEF; $i++)
	{
		$text = unichr($i);
		$box = imagettfbbox($FONT_SIZE, 0, $font, $text);
		
//		arr($box);

		$x_width = abs($box[4] - $box[0]);
		$y_width = abs($box[5] - $box[1]);
		
		$x_size = $x_width + $CORR_BOX_X_SIZE;
		$y_size = $y_width + $CORR_BOX_Y_SIZE;
		
		$x_size += (@$CORRECTIONS[$FONT_NAME][$index]["spec"][$text]["box-x-size"]) + 0;
		
		$x_min = min($box[4], $box[0]);
		$y_min = min($box[5], $box[1]);
		
		if ($x_width == 0)
		{
			$indices_arr[$i] = -1;
			$sizes_arr[$i] = 0;
		}
		else
		{
			$indices_arr[$i] = $char_index;
			$sizes_arr[$i] = $x_size + 1;
		}

		if ($x_width == 0)
			continue;
			
		if (($x + $x_size) >= $IMG_SIZE)
		{
			$x = 0;
			$y += $line_height + 1;
		};
		
		if (($i % 2) == 0)
			$col = $color1;
		else
			$col = $color2;
		
		if ($DEBUGGING)
			imagerectangle($im, $x, $y, $x + $x_size, $y + $line_height, $col);
			
		$class .= "
		".sprintf("%1.7ff, %1.7ff, %1.7ff, %1.7ff, // 0x%04x (%d) = '%s'", $x / $img_dim, $y / $img_dim, ($x + $x_size) / $img_dim, ($y + $line_height) / $img_dim, $i, $i, $text)."";
		
		imagettftext($im, $FONT_SIZE, 0, $x + $CORR_X_POS, $y - $dim[1] + $CORR_Y_POS, $color, $font, $text);
		
		$x += $x_size + 1;
		$char_index++;
	};
	
	$indices .= "
	
	public static final int[] FONT_CHAR_INDEX = new int[]
	{";
	
	$sizes .= "
	
	//characters' height in pixels
	public static final int FONT_CHAR_HEIGHT = $line_height;
	
	//characters' width in pixels
	public static final int[] FONT_CHAR_WIDTH = new int[]
	{";

	for ($i = $FIRST_CHAR_DEF; $i <= $LAST_CHAR_DEF; $i++)
	{
		$text = unichr($i);
		$comment = sprintf("0x%04x (%d) = '%s'", $i, $i, $text);	

		if (@$indices_arr[$i] == -1)
		{
			$text = "";
			$comment = sprintf("0x%04x (%d)", $i, $i);
		};
			
		$indices .= "
		".sprintf("%4d, // %s", @$indices_arr[$i], $comment)."";
		
		$sizes .= "
		".sprintf("%4d, // %s", @$sizes_arr[$i], $comment)."";
		
		if ($i % 8 == 7)
		{
			$indices .= "
";
			$sizes .= "
";

		};

	};
	$indices .= "
	};";
	$sizes .= "
	};";	

	$class .= "
	};";
	
	
	$class .= $indices;
	$class .= $sizes;
	
	$class .= "
	
	public $class_name()
	{
		super.FONT_MIN_INDEX = FONT_MIN_INDEX;
		super.FONT_MAX_INDEX = FONT_MAX_INDEX;
		super.FONT_NAME = FONT_NAME;
		super.FONT_SIZE = FONT_SIZE;
		super.FONT_TEXTURE_COORDS = FONT_TEXTURE_COORDS;
		super.FONT_TEXTURE_HEIGHT = FONT_TEXTURE_HEIGHT;
		super.FONT_TEXTURE_WIDTH = FONT_TEXTURE_WIDTH;
		super.FONT_CHAR_HEIGHT = FONT_CHAR_HEIGHT;
		super.FONT_CHAR_WIDTH = FONT_CHAR_WIDTH;
		super.FONT_TEXTURE_NAME = FONT_TEXTURE_NAME;
	};
}";

	if (!$DEBUGGING)
	{
		file_put_contents($class_name.".java", $class);
		imagepng($im, $file_name.".png");
		
		echo "Class [$class_name.java] generated<br />";
		echo "File [$file_name.png] written<br />";
		exit;
	};
	
	header ('Content-Type: image/png');
	imagepng($im);
	imagedestroy($im);
?>
