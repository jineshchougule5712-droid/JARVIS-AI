import {chromium} from 'playwright';
const browser=await chromium.launch({headless:false});
const page=await browser.newPage();
await page.goto(process.env.TARGET_URL||'https://www.google.com');
console.log('JARVIS browser worker ready.');
console.log('Login, OTP, CAPTCHA and final consequential submission require human control.');
await new Promise(()=>{});