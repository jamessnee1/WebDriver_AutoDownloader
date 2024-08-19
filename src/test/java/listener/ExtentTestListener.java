package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import helpers.ConfigHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;

public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        // Initialize Extent Reports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") +
                ConfigHelper.getInstance().getProperty("reportLocation") + "extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test in the report
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Log test success
        test.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Capture screenshot on failure
        String screenshotPath = captureScreenshot(result.getMethod().getMethodName());
        test.get().fail(result.getThrowable())
                .addScreenCaptureFromPath(screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Log test skipped
        test.get().skip("Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Can be used to log tests that failed but are within the success percentage
    }

    // Method to capture screenshot
    private String captureScreenshot(String methodName) {
        // Ensure you have access to WebDriver instance here
        WebDriver driver = BaseTest.getDriver();
        if(driver == null){
            return "Driver is not available";
        }
        String screenshotPath = System.getProperty("user.dir") + ConfigHelper.getInstance().getProperty("screenshotLocation") + methodName + ".png";
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(screenshotPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshotPath;
    }
}
