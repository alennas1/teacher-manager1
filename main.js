const { app, BrowserWindow } = require('electron');
const { spawn } = require('child_process');
const path = require('path');

let mainWindow;
let backendProcess;

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      nodeIntegration: false,
    },
  });

  // Start Spring Boot JAR
  const jarPath = path.join(__dirname, 'backend', 'backend.jar');
  backendProcess = spawn('java', ['-jar', jarPath]);

  backendProcess.stdout.on('data', (data) => {
    console.log(`[Spring Boot] ${data}`);
  });

  backendProcess.stderr.on('data', (data) => {
    console.error(`[Spring Boot ERROR] ${data}`);
  });

  backendProcess.on('close', (code) => {
    console.log(`[Spring Boot] process exited with code ${code}`);
  });

  // Wait a bit before opening the frontend
  setTimeout(() => {
    mainWindow.loadURL('http://localhost:8080'); // Spring Boot runs here
  }, 5000); // wait 5 sec to let backend start

  mainWindow.on('closed', () => {
    mainWindow = null;
    if (backendProcess) backendProcess.kill();
  });
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
  if (backendProcess) backendProcess.kill();
  if (process.platform !== 'darwin') app.quit();
});
