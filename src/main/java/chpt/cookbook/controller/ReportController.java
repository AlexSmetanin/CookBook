package chpt.cookbook.controller;

import chpt.cookbook.OpenFilePDF;
import chpt.cookbook.entity.Recipe;
import chpt.cookbook.repository.RecipeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ReportController {
    @Autowired
    private RecipeRepository recipeRepository;
    private static BaseFont baseFont;
    static {
        try {
            baseFont = loadBaseFont(getFilePath("fonts/times.ttf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilePath(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        InputStream input = resource.getInputStream();
        File file = resource.getFile();
        return file.getAbsolutePath();
    }

    private static Font font = new Font(baseFont, 9, Font.NORMAL, BaseColor.BLACK);
    private static Font font1 = new Font(baseFont, 14, Font.BOLD, BaseColor.BLACK);
    private static Font font2 = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);

    public ReportController(RecipeRepository recipeRepository) {
        super();
        this.recipeRepository = recipeRepository;
    }

    // Обробка запиту для звіту
    @GetMapping("/recipes/printRecipe")
    public String processReportParams(Model model, @RequestParam int id)
            throws IOException, DocumentException {
        try {
            Recipe recipe = recipeRepository.findById((long) id).get();
            model.addAttribute("recipe", recipe);
            createReport(recipe);
        }
        catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "main";
        }
        return "recipes/recipeDetails";
    }

    // Формування звіту
    private void createReport(Recipe recipe) throws IOException, DocumentException {
        // створення екземпляру об’єкту документу
        Document document = new Document(PageSize.A4, 25, 10, 20, 20);
        // створення об’єкту редактора для pdf файлів PdfWriter
        PdfWriter pdfWriter = PdfWriter.getInstance(document,
                new FileOutputStream("src/main/resources/Document.pdf"));
        document.open();

        Paragraph p = new Paragraph(recipe.getDishName(), font1);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        p = new Paragraph(recipe.getDescription(), font2);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        // Малюнок
        String imagePath = Paths.get(
                "public",
                "images" + recipe.getImageFileName()
        ).toAbsolutePath().toString();

        Image image = Image.getInstance(imagePath);
        image.scaleToFit(120f, 120f);
        image.setAlignment(1);
        document.add(image);

        // Категорія
        p = new Paragraph("Категорія: " + recipe.getCategory(), font);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(10f); // відступ після абзацу (в пунктах)
        document.add(p);

        p = new Paragraph("Інгредієнти: " +recipe.getIngredients(), font);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(10f); // відступ після абзацу (в пунктах)
        document.add(p);

        p = new Paragraph("Інструкція: " +recipe.getInstruction(), font);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(10f); // відступ після абзацу (в пунктах)
        document.add(p);

        p = new Paragraph("Калорійність: " +recipe.getCalories() + " ККал", font);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        document.close();
        OpenFilePDF.openFile(getFilePath("Document.pdf"));
    }

    /**
     * Загружаем шрифт из .ttf файла
     *
     * @param fontName Путь к файлу
     * @return
     */
    private static BaseFont loadBaseFont(String fontName) {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseFont;
    }
}
