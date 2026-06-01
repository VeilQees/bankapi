$output = "project_dump.txt"

if (Test-Path $output) {
    Remove-Item $output
}

Get-ChildItem -Path . -Recurse -File |
Where-Object {

    $_.FullName -notmatch "\\target\\" -and
    $_.FullName -notmatch "\\.idea\\" -and
    $_.FullName -notmatch "\\.git\\" -and
    (
        $_.Extension -eq ".java" -or
        $_.Extension -eq ".html" -or
        $_.Extension -eq ".js" -or
        $_.Extension -eq ".css" -or
        $_.Extension -eq ".yml" -or
        $_.Extension -eq ".yaml" -or
        $_.Extension -eq ".properties"
    )

} | ForEach-Object {

    Add-Content $output "`n`n==============================="
    Add-Content $output "FILE: $($_.FullName)"
    Add-Content $output "===============================`n"

    Get-Content $_.FullName | Add-Content $output
}

Write-Host "DONE: project_dump.txt"