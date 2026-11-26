package experiments;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/** Minimal Java2D line-chart renderer (no external libraries). */
public class ChartWriter {
    private static final Color[] COLORS = {
        new Color(0x1f77b4), new Color(0xd62728), new Color(0x2ca02c), new Color(0xff7f0e),
        new Color(0x9467bd), new Color(0x8c564b), new Color(0x17becf), new Color(0x7f7f7f), new Color(0xbcbd22), new Color(0xe377c2)};

    public static void line(File out, String title, String xLabel, String yLabel,
                            double[] xs, LinkedHashMap<String, double[]> series) throws IOException {
        System.setProperty("java.awt.headless", "true");
        int W = 820, H = 520, L = 80, R = 200, T = 60, B = 70;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE); g.fillRect(0, 0, W, H);

        double yMax = 0;
        for (double[] ys : series.values()) for (double y : ys) yMax = Math.max(yMax, y);
        double step = niceStep(yMax / 6.0);
        yMax = Math.max(step, Math.ceil(yMax / step) * step);
        double xMin = xs[0], xMax = xs[xs.length - 1];
        int pw = W - L - R, ph = H - T - B;

        g.setFont(new Font("SansSerif", Font.BOLD, 17)); g.setColor(Color.BLACK);
        g.drawString(title, L, 35);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (double v = 0; v <= yMax + 1e-9; v += step) {
            int y = T + ph - (int) (ph * v / yMax);
            g.setColor(new Color(0xe0e0e0)); g.drawLine(L, y, L + pw, y);
            g.setColor(Color.BLACK); String s = fmt(v); g.drawString(s, L - 8 - g.getFontMetrics().stringWidth(s), y + 4);
        }
        for (double x : xs) {
            int px = L + (xMax == xMin ? pw / 2 : (int) (pw * (x - xMin) / (xMax - xMin)));
            g.setColor(new Color(0xe0e0e0)); g.drawLine(px, T, px, T + ph);
            g.setColor(Color.BLACK); String s = fmt(x); g.drawString(s, px - g.getFontMetrics().stringWidth(s) / 2, T + ph + 20);
        }
        g.setColor(Color.BLACK); g.drawRect(L, T, pw, ph);
        g.drawString(xLabel, L + pw / 2 - g.getFontMetrics().stringWidth(xLabel) / 2, H - 20);
        Graphics2D gr = (Graphics2D) g.create(); gr.rotate(-Math.PI / 2);
        gr.drawString(yLabel, -(T + ph / 2 + gr.getFontMetrics().stringWidth(yLabel) / 2), 22); gr.dispose();

        int ci = 0, ly = T + 10;
        g.setStroke(new BasicStroke(2.2f));
        for (Map.Entry<String, double[]> e : series.entrySet()) {
            Color c = COLORS[ci++ % COLORS.length]; g.setColor(c);
            double[] ys = e.getValue(); int px0 = 0, py0 = 0;
            for (int i = 0; i < xs.length; i++) {
                int px = L + (xMax == xMin ? pw / 2 : (int) (pw * (xs[i] - xMin) / (xMax - xMin)));
                int py = T + ph - (int) (ph * ys[i] / yMax);
                if (i > 0) g.drawLine(px0, py0, px, py);
                g.fillOval(px - 4, py - 4, 8, 8); px0 = px; py0 = py;
            }
            g.drawLine(L + pw + 15, ly, L + pw + 40, ly); g.fillOval(L + pw + 22, ly - 4, 8, 8);
            g.setColor(Color.BLACK); g.drawString(e.getKey(), L + pw + 46, ly + 4); ly += 22;
        }
        g.dispose();
        out.getParentFile().mkdirs();
        ImageIO.write(img, "png", out);
    }

    private static double niceStep(double raw) {
        if (raw <= 0) return 1;
        double mag = Math.pow(10, Math.floor(Math.log10(raw))), f = raw / mag;
        return (f <= 1 ? 1 : f <= 2 ? 2 : f <= 5 ? 5 : 10) * mag;
    }
    private static String fmt(double v) { return v == Math.rint(v) ? String.valueOf((long) v) : String.format("%.1f", v); }
}
