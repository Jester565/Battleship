package JBasics;

import java.util.ArrayList;

public class Message{
	private String s = null;
	private ArrayList<String> messages;
	private int lastWidth = 0;
	private int lastFontSize = 0;
	Message(String s){
		this.s = s;
		messages = new ArrayList<String>();
	}
	public int draw(ShapeRenderer sr, int x, int y, int w, int messageHeight, int fontSize, float alpha){
		if(w != lastWidth || lastFontSize != fontSize){
			lastWidth = w;
			lastFontSize = fontSize;
			messages = spacedStrings(w, fontSize);
		}
		for(int i = messages.size()-1; i >= 0; i--){
			sr.drawRectangle(true, x-(int)(fontSize/12d), y - (int)(fontSize/3.1d) + messageHeight, (int)(ShapeRenderer.getFontWidth(messages.get(i), fontSize)*ShapeRenderer.xScale + fontSize/6d), (int)(fontSize * 1.15f),1,1,1,alpha/2.6f);
			sr.drawText(messages.get(i), x, y + messageHeight,fontSize,0,0,0,alpha);
			messageHeight += fontSize;
		}
		return messageHeight;
	}
	public int draw(ShapeRenderer sr, int x, int y, int w, int messageHeight, int fontSize, float alpha, float r, float g, float b){
		if(w != lastWidth || lastFontSize != fontSize){
			lastWidth = w;
			lastFontSize = fontSize;
			messages = spacedStrings(w, fontSize);
		}
		for(int i = messages.size()-1; i >= 0; i--){
			sr.drawRectangle(true, x-(int)(fontSize/12d), y - (int)(fontSize/3.1d) + messageHeight, (int)(ShapeRenderer.getFontWidth(messages.get(i), fontSize)*ShapeRenderer.xScale + fontSize/6d), (int)(fontSize * 1.15f),1,1,1,alpha/2.6f);
			sr.drawText(messages.get(i), x, y + messageHeight,fontSize,r,g,b,alpha);
			messageHeight += fontSize;
		}
		return messageHeight;
	}
	public int draw(ShapeRenderer sr, int x, int y, int w, int messageHeight, int fontSize, float alpha, float r, float g, float b, boolean textBackGround){
		if(w != lastWidth || lastFontSize != fontSize){
			lastWidth = w;
			lastFontSize = fontSize;
			messages = spacedStrings(w, fontSize);
		}
		for(int i = messages.size()-1; i >= 0; i--){
			if(textBackGround)
				sr.drawRectangle(true, x-(int)(fontSize/12d), y - (int)(fontSize/3.1d) + messageHeight, (int)(ShapeRenderer.getFontWidth(messages.get(i), fontSize)*ShapeRenderer.xScale + fontSize/6d), (int)(fontSize * 1.15f),1,1,1,alpha/2.6f);
			sr.drawText(messages.get(i), x, y + messageHeight,fontSize,r,g,b,alpha);
			messageHeight += fontSize;
		}
		return messageHeight;
	}
	public int draw(ShapeRenderer sr, int x, int y, int w, int messageHeight, int fontSize, float alpha, float r, float g, float b, boolean textBackGround, int spacingHeight){
		if(w != lastWidth || lastFontSize != fontSize){
			lastWidth = w;
			lastFontSize = fontSize;
			messages = spacedStrings(w, fontSize);
		}
		for(int i = messages.size()-1; i >= 0; i--){
			if(textBackGround)
				sr.drawRectangle(true, x-(int)(fontSize/12d), y - (int)(fontSize/3.1d) + messageHeight, (int)(ShapeRenderer.getFontWidth(messages.get(i), fontSize)*ShapeRenderer.xScale + fontSize/6d), (int)(fontSize * 1.15f),1,1,1,alpha/2.6f);
			sr.drawText(messages.get(i), x, y + messageHeight,fontSize,r,g,b,alpha);
			messageHeight += (fontSize + spacingHeight);
		}
		return messageHeight;
	}
	private ArrayList<String> spacedStrings(int w, int fontSize){
		int lastSpace = 0;
		int lastReturn = 0;
		
		ArrayList<Integer> returnInts = new ArrayList<Integer>();
		returnInts.add(0);
		for(int i = 0; i < s.length(); i++){
			if(s.substring(i,i+1).equals(" "))
				lastSpace = i;
			if(ShapeRenderer.getFontWidth(s, fontSize, lastReturn, i+1)*ShapeRenderer.xScale>w-10){
				if(lastSpace > lastReturn){
				returnInts.add(lastSpace+1);
				lastReturn = lastSpace+1;
				}
				else{
					returnInts.add(i+1);
					lastReturn = i + 1;
				}
			}
		}
		ArrayList<String> strings = new ArrayList<String>();
		for(int i = 0; i < returnInts.size()-1; i++){
			strings.add(s.substring(returnInts.get(i), returnInts.get(i+1)));
		}
		if(returnInts.size() > 0){
			strings.add(s.substring(returnInts.get(returnInts.size()-1)));
		}else{
			strings.add(s);
		}
		return strings;
	}
}