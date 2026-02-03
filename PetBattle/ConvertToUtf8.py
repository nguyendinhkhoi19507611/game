
import os
import glob

def convert_to_utf8(directory):
    files = glob.glob(os.path.join(directory, '**', '*.java'), recursive=True)
    for file_path in files:
        try:
            content = None
            encoding = None
            
            # Try parsing as UTF-8 first
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                encoding = 'utf-8'
            except UnicodeDecodeError:
                # If fail, try GBK
                try:
                    with open(file_path, 'r', encoding='gbk') as f:
                        content = f.read()
                    encoding = 'gbk'
                except UnicodeDecodeError:
                    print(f"Skipping {file_path}: Unknown encoding")
                    continue
            
            # If it was GBK, write back as UTF-8
            if encoding == 'gbk':
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f"Converted {file_path} from GBK to UTF-8")
            else:
                # It's already UTF-8, but might need normalization or just ensure it is saved clean? 
                # Actually if it read as UTF-8 cleanly, we don't strictly need to rewrite unless we mixed content manually before.
                # But to be safe, we can just leave it.
                pass
                
        except Exception as e:
            print(f"Error processing {file_path}: {e}")

if __name__ == "__main__":
    convert_to_utf8('d:\\game\\PetBattle\\PetBattleServer\\src')
