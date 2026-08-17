param(
    [string]$LogDirectory = (Join-Path $PSScriptRoot 'log'),
    [long]$MaxBytes = 50MB
)

New-Item -ItemType Directory -Force -Path $LogDirectory | Out-Null

Get-ChildItem -LiteralPath $PSScriptRoot -File -Filter '*.log' |
    Move-Item -Destination $LogDirectory -Force

function Get-LogFiles {
    Get-ChildItem -LiteralPath $LogDirectory -File -Filter '*.log' |
        Sort-Object LastWriteTime, CreationTime, Name
}

$files = @(Get-LogFiles)
$total = ($files | Measure-Object -Property Length -Sum).Sum
while ($total -gt $MaxBytes -and $files.Count -gt 0) {
    $oldest = $files[0]
    Remove-Item -LiteralPath $oldest.FullName -Force
    $total -= $oldest.Length
    $files = @(Get-LogFiles)
}

Write-Output ("Log directory: {0}" -f $LogDirectory)
Write-Output ("Log size: {0:N0}/{1:N0} bytes" -f $total, $MaxBytes)
