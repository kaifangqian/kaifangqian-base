const fs = require('fs');
const path = require('path');

// 许可证头部内容
const LICENSE_HEADER = `/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */`;

// 递归查找文件
function findFiles(dir, extensions) {
  let results = [];
  const list = fs.readdirSync(dir);
  
  list.forEach(file => {
    file = path.resolve(dir, file);
    const stat = fs.statSync(file);
    
    if (stat && stat.isDirectory()) {
      results = results.concat(findFiles(file, extensions));
    } else {
      const ext = path.extname(file).substring(1);
      if (extensions.includes(ext)) {
        results.push(file);
      }
    }
  });
  
  return results;
}

// 检查文件是否已包含许可证
function hasLicenseHeader(content) {
  return content.includes('Copyright (C) [2025]') || 
         content.includes('开放签') ||
         content.includes('AGPLv3');
}

// 为文件添加许可证头部
function addLicenseToFile(filePath) {
  try {
    const content = fs.readFileSync(filePath, 'utf8');
    
    if (hasLicenseHeader(content)) {
      console.log(`✓ License already exists in ${filePath}`);
      return false;
    }
    
    const newContent = LICENSE_HEADER + '\n\n' + content;
    fs.writeFileSync(filePath, newContent, 'utf8');
    console.log(`✓ Added license to ${filePath}`);
    return true;
  } catch (error) {
    console.error(`✗ Error processing ${filePath}:`, error.message);
    return false;
  }
}

// 主函数
function main() {
  const extensions = ['js', 'ts'];
  console.log('Searching for files...');
  
  const files = findFiles('./src', extensions);
  console.log(`Found ${files.length} files`);
  
  let updated = 0;
  files.forEach(file => {
    if (addLicenseToFile(file)) {
      updated++;
    }
  });
  
  console.log(`\nCompleted: ${updated} files updated with license header`);
}

main();