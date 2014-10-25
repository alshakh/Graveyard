/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Util {
	/*
	Generate Random ID
	*/
	private static final char[] CHARSET = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789".toCharArray();
	private static final int LENGTH = 12;
	private static final Set<String> _generatedIds = new HashSet<String>();
	public static String generateRandomId(){
		Random random = new SecureRandom();
		char[] result = new char[LENGTH+1];
		result[0] = 'a'; // to ensure starting with a letter
		for (int i = 1; i < result.length; i++) {
			int randomCharIndex = random.nextInt(CHARSET.length);
			result[i] = CHARSET[randomCharIndex];
		}
		String id = new String(result);
		//+ avoid duplication
		if(_generatedIds.contains(id)) return generateRandomId();
		_generatedIds.add(id);
		//-
		return id;
	}

}
