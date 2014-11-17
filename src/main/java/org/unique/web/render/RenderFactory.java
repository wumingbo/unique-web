package org.unique.web.render;

import org.unique.Const;
import org.unique.support.render.ErrorRender;
import org.unique.support.render.HtmlRender;
import org.unique.support.render.JavascriptRender;
import org.unique.support.render.JspRender;
import org.unique.support.render.RedirectRender;
import org.unique.support.render.TextRender;

/**
 * RenderFactory.
 */
public class RenderFactory {

    private RenderFactory() {
    }
    
    public static RenderFactory single() {
        return RenderFactoryHoder.instance;
    }

    private static class RenderFactoryHoder {

        private static final RenderFactory instance = new RenderFactory();
    }
    
    public Render getTextRender(String text) {
        return new TextRender(text);
    }

    public Render getTextRender(String text, String contentType) {
        return new TextRender(text, contentType);
    }

    public Render getDefaultRender() {
        if (Const.RENDER_TYPE.equalsIgnoreCase("jsp")) {
            return new JspRender();
        }
		return null;
    }
    
    public Render getErrorRender(int errorCode, String view) {
        return new ErrorRender(errorCode, view);
    }

    public Render getErrorRender(int errorCode) {
        return new ErrorRender(errorCode, Const.getConfig("errorCode"));
    }

    public Render getRedirectRender(String url) {
        return new RedirectRender(url);
    }

    public Render getRedirectRender(String url, boolean withQueryString) {
        return new RedirectRender(url, withQueryString);
    }

    public Render getJavascriptRender(String jsText) {
        return new JavascriptRender(jsText);
    }

    public Render getHtmlRender(String htmlText) {
        return new HtmlRender(htmlText);
    }

}
