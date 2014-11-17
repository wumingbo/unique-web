package org.unique.web.render;

import org.unique.Const;
import org.unique.web.render.impl.ErrorRender;
import org.unique.web.render.impl.HtmlRender;
import org.unique.web.render.impl.JavascriptRender;
import org.unique.web.render.impl.JspRender;
import org.unique.web.render.impl.TextRender;

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

    /**
     * 获取默认渲染器
     * @param view 视图
     * @return Render
     */
    public Render getRender(String view) {
        return getDefaultRender(view);
    }

    public Render getJspRender(String view) {
        return new JspRender(view);
    }

    public Render getTextRender(String text) {
        return new TextRender(text);
    }

    public Render getTextRender(String text, String contentType) {
        return new TextRender(text, contentType);
    }

    public Render getDefaultRender() {
        if (Const.RENDER_TYPE == RenderType.JSP) {
            return new JspRender();
        }
		return null;
    }
    
    public Render getDefaultRender(String view) {
        return new JspRender(view);
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
